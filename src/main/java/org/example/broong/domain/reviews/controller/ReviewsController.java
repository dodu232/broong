package org.example.broong.domain.reviews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewResponseDto;
import org.example.broong.domain.reviews.service.ReviewsService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 리뷰 컨트롤러 클래스
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;


    // 리뷰 생성 API
    @PostMapping("/{orderId}")
    public ResponseEntity<Void> create(
            AuthUser authUser,
            @PathVariable Long orderId,
            @Valid @RequestBody CreateReviewRequestDto createReviewRequestDto
    ) {
        // 임시 코드 @AuthenticationPrincipal User user
        reviewsService.create(authUser.getId(), orderId, createReviewRequestDto);
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
            AuthUser authUser,
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
            AuthUser authUser, @PathVariable Long reviewId
    ) {
        reviewsService.deleteById(authUser.getId(), reviewId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


}
