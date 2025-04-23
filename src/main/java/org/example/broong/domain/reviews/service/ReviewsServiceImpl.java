package org.example.broong.domain.reviews.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
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
    private final StoresRepository storesRepository;

    // 리뷰 생성 메서드
    @Override
    public void create(Users users, Orders orders, CreateReviewRequestDto createReviewRequestDto) {
        Reviews reviews = new Reviews(users, orders, createReviewRequestDto);
        reviewsRepository.save(reviews);
    }

    // storeId 기준 리뷰 조회 메서드
    @Override
    public List<FindReviewByStoreResponseDto> findByStore(Long storeId) {
        Stores stores = storesRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다."));
        List<Reviews> reviewsList = reviewsRepository.findAllByStoreId(stores);
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
        if (!reviews.getUserId().getId == userId) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "본인의 리뷰만 수정할 수 있습니다.");
        }
        reviews.update(updateReviewRequestDto);
        return new UpdateReviewResponseDto(reviews);
    }

    // 리뷰 id 기준 리뷰 삭제 메서드
    @Override
    public void deleteById(Long userId, Long reviewId) {
        Reviews reviews = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 리뷰입니다."));
        if (!reviews.getUserId().getId == userId) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "본인의 리뷰만 삭제할 수 있습니다.");
        }
        reviewsRepository.deleteById(reviewId);
    }
}
