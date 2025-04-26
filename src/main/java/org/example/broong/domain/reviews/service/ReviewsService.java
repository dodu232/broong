package org.example.broong.domain.reviews.service;

import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewsService {
    void create(Long userId, Long orderId, CreateReviewRequestDto createReviewRequestDto);

    Slice<FindReviewByStoreResponseDto> getReviewsListByStore(Long storeId, Pageable pageable);

    UpdateReviewResponseDto updateById(Long userId, Long reviewId, UpdateReviewRequestDto updateReviewRequestDto);

    void deleteById(Long userId, Long reviewId);
}