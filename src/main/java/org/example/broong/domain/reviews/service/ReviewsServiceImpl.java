package org.example.broong.domain.reviews.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.testOrder.OrdersRepository;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 리뷰 서비스 클래스
@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final StoreRepository storesRepository;
    private final OrdersRepository ordersRepository;
    private final UserService userService;

    // 로그인 유저 확인
//    public static void loginUserTest(Long userId) {

//    }
//    ReviewsServiceImpl.loginUserTest(userId);

    // 리뷰 생성 메서드
    @Override
    public void create(Long userId, Long orderId, CreateReviewRequestDto requestDto) {
        if (userId == null) {
            new ApiException(HttpStatus.FORBIDDEN, ErrorType.NOT_LOGGED_IN, "로그인이 필요한 사용자입니다.");
        }
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 주문입니다."));
        if (userId == orderId) {
            new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER, "본인의 주문에만 리뷰를 남길 수 있습니다.");
        }
        if (reviewsRepository.existsByOrderId_Id(orderId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "리뷰를 작성하신 주문입니다.");
        }
        if (order.getStore() == null || order.getStore().getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다.");
        }
        Store store = order.getStore();
        Reviews newReview = new Reviews(userService.getById(userId), order, store, requestDto.getRating(), requestDto.getContents());
        reviewsRepository.save(newReview);
    }

    // storeId 기준 리뷰 조회 메서드
    @Override
    public List<FindReviewByStoreResponseDto> findByStore(Long storeId) {
        Store store = storesRepository.findById(storeId)
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
    @Transactional
    public UpdateReviewResponseDto updateById(Long userId, Long reviewId, UpdateReviewRequestDto requestDto) {
        if (userId == null) {
            new ApiException(HttpStatus.FORBIDDEN, ErrorType.NOT_LOGGED_IN, "로그인이 필요한 사용자입니다.");
        }
        Reviews reviews = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다."));
        if (reviews.getUserId().getId() != userId) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "본인의 리뷰만 수정할 수 있습니다.");
        }
        if (reviews.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다.");
        }
        if (reviews.getStoreId() == null || reviews.getStoreId().getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다.");
        }
        reviews.update(requestDto.getRating(), requestDto.getContents());
        return new UpdateReviewResponseDto(reviews);
    }

    // 리뷰 id 기준 리뷰 삭제 메서드
    @Override
    @Transactional
    public void deleteById(Long userId, Long reviewId) {
        Reviews reviews = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다."));
        if (reviews.getUserId().getId() != userId) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "본인의 리뷰만 삭제할 수 있습니다.");
        }
        if (reviews.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다.");
        }
        reviewsRepository.delete(reviews);
    }
}