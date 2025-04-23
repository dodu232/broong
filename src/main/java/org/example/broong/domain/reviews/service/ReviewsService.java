package org.example.broong.domain.reviews.service;

import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;

import java.util.List;

public interface ReviewsService {
    void create(Users users, Orders orders, CreateReviewRequestDto createReviewRequestDto);

    List<FindReviewByStoreResponseDto> findByStore(Long storeId);

    UpdateReviewResponseDto updateById(Long userId, Long reviewId, UpdateReviewRequestDto updateReviewRequestDto);

    void deleteById(Long userId, Long reviewId);
}