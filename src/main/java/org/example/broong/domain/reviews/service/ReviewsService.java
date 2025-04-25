package org.example.broong.domain.reviews.service;

import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.user.entity.User;

import java.util.List;

public interface ReviewsService {
    void create(User users, Long order_id, CreateReviewRequestDto createReviewRequestDto);

    List<FindReviewByStoreResponseDto> findByStore(Long storeId);

    UpdateReviewResponseDto updateById(Long userId, Long reviewId, UpdateReviewRequestDto updateReviewRequestDto);

    void deleteById(Long userId, Long reviewId);
}