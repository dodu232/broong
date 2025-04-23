package org.example.broong.domain.reviews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 리뷰 업데이트 request dto 클래스
@Getter
@RequiredArgsConstructor
public class UpdateReviewRequestDto {

    @NotBlank
    private final int rating;

    @NotBlank
    private final String contents;
}
