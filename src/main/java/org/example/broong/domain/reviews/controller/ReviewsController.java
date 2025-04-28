package org.example.broong.domain.reviews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.service.ReviewsService;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 리뷰 컨트롤러 클래스
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;


    // 리뷰 생성 API
    @PostMapping("/{orderId}")
    public ResponseEntity<Void> create(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long orderId,
        @Valid @RequestBody CreateReviewRequestDto createReviewRequestDto
    ) {
        // 임시 코드 @AuthenticationPrincipal User user
        reviewsService.create(userDetails.getUserId(), orderId, createReviewRequestDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    // 가게 기준 리뷰 다건 페이징 조회 API
    @GetMapping("/{storeId}")
    public ResponseEntity<Slice<FindReviewByStoreResponseDto>> getReviewsList(
        @PathVariable Long storeId,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewsService.getReviewsListByStore(storeId, pageable));
    }

    // 리뷰 id 기준 리뷰 수정 API
    @PatchMapping("/{reviewId}")
    public ResponseEntity<UpdateReviewResponseDto> updateById(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody UpdateReviewRequestDto requestDto,
        @PathVariable Long reviewId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewsService.updateById(userDetails.getUserId(), reviewId, requestDto));
    }

    // 리뷰 id 기준 리뷰 삭제 API
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long reviewId
    ) {
        reviewsService.deleteById(userDetails.getUserId(), reviewId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }


}
