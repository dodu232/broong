package org.example.broong.domain.reviews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.service.ReviewsService;
import org.example.broong.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 리뷰 컨트롤러 클래스
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;


    // 리뷰 생성 API
    @PostMapping("/{orderId}")
    public ResponseEntity<Void> create(
            @Auth AuthUser authUser,
            @PathVariable Long orderId,
            @Valid @RequestBody CreateReviewRequestDto createReviewRequestDto
    ) {
        // 임시 코드 @AuthenticationPrincipal User user
        reviewsService.create(authUser.getId(), orderId, createReviewRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // 가게 기준 리뷰 다건 조회 API
    @GetMapping("/{storeId}")
    public ResponseEntity<List<FindReviewByStoreResponseDto>> getByStore(
            @PathVariable Long storeId
    ) {
        List<FindReviewByStoreResponseDto> storeReviews = reviewsService.findByStore(storeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeReviews);
    }

    // 리뷰 id 기준 리뷰 수정 API
    @PatchMapping("/{reviewId}")
    public ResponseEntity<UpdateReviewResponseDto> updateById(
            @Auth AuthUser authUser,
            @Valid @RequestBody UpdateReviewRequestDto requestDto,
            @PathVariable Long reviewId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reviewsService.updateById(authUser.getId(), reviewId, requestDto));
    }

    // 리뷰 id 기준 리뷰 삭제 API
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Auth AuthUser authUser, @PathVariable Long reviewId
    ) {
        reviewsService.deleteById(authUser.getId(), reviewId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
