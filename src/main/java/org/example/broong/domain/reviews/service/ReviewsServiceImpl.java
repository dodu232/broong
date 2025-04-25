package org.example.broong.domain.reviews.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.store.entity.Stores;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.testOrder.OrdersRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 리뷰 서비스 클래스
@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final StoreRepository storesRepository;
    private final OrdersRepository ordersRepository;

    // 리뷰 생성 메서드
    @Override
    public void create(User user, Long order_id, CreateReviewRequestDto createReviewRequestDto) {
        Orders order = ordersRepository.findById(order_id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 주문입니다."));
        if (reviewsRepository.existsByOrderId_Id(order_id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "리뷰를 작성하신 주문입니다.");
        }
        if (order.getStore() == null || order.getStore().getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다.");
        }
        Stores store = order.getStore();
        Reviews newReview = new Reviews(user, order, store, createReviewRequestDto);
        reviewsRepository.save(newReview);
    }

    // storeId 기준 리뷰 조회 메서드
    @Override
    public List<FindReviewByStoreResponseDto> findByStore(Long storeId) {
        Stores store = storesRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다."));
        if (store.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다.");
        }
        List<Reviews> reviewsList = reviewsRepository.findAllByStoreIdAndUserId_DeletedAtIsNull(store);
        List<FindReviewByStoreResponseDto> reviewsDtoList = new ArrayList<>();
        for (Reviews reviews : reviewsList) {
            reviewsDtoList.add(new FindReviewByStoreResponseDto(reviews));
        }
        return reviewsDtoList;
    }

    // 리뷰 id 기준 리뷰 수정 메서드
    @Override
    public UpdateReviewResponseDto updateById(Long userId, Long reviewId, UpdateReviewRequestDto updateReviewRequestDto) {
        Reviews reviews = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다."));
        if (!(reviews.getUserId().getId() == userId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "본인의 리뷰만 수정할 수 있습니다.");
        }
        if (reviews.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다.");
        }
        if (reviews.getStoreId() == null || reviews.getStoreId().getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다.");
        }
        reviews.update(updateReviewRequestDto);
        return new UpdateReviewResponseDto(reviews);
    }

    // 리뷰 id 기준 리뷰 삭제 메서드
    @Override
    public void deleteById(Long userId, Long reviewId) {
        Reviews reviews = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다."));
        if (!(reviews.getUserId().getId() == userId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "본인의 리뷰만 삭제할 수 있습니다.");
        }
        if (reviews.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "존재하지 않는 리뷰입니다.");
        }
        reviews.setDeletedAt();
        reviewsRepository.save(reviews);
    }
}