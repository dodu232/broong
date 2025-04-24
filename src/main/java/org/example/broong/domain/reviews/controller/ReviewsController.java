package org.example.broong.domain.reviews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.service.ReviewsService;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.testOrder.OrdersRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 리뷰 컨트롤러 클래스
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    // 리뷰 생성 API
    @PostMapping("/{order_id}")
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateReviewRequestDto createReviewRequestDto,
            @PathVariable Long order_id) {
        Orders orders = ordersRepository.findById(order_id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 주문입니다."));
        reviewsService.create(user, orders, createReviewRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 가게 기준 리뷰 다건 조회 API
    @GetMapping("/{store_id}")
    public ResponseEntity<List<FindReviewByStoreResponseDto>> findByStore(@PathVariable Long store_id) {
        List<FindReviewByStoreResponseDto> storeReviews = reviewsService.findByStore(store_id);
        return ResponseEntity.status(HttpStatus.OK).body(storeReviews);
    }

    // 리뷰 id 기준 리뷰 수정 API
    @PatchMapping("/{review_id}")
    public ResponseEntity<UpdateReviewResponseDto> updateById(
            @AuthenticationPrincipal User users,
            @Valid @RequestBody UpdateReviewRequestDto updateReviewRequestDto,
            @PathVariable Long review_id) {

        UpdateReviewResponseDto updateReviewResponseDto = reviewsService.updateById(users.getId(), review_id, updateReviewRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateReviewResponseDto);
    }

    // 리뷰 id 기준 리뷰 삭제 API
    @DeleteMapping("/{review_id}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal User users, @PathVariable Long review_id) {
        reviewsService.deleteById(users.getId(), review_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
