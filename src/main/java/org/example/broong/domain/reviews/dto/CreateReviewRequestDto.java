package org.example.broong.domain.reviews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 리뷰 생성 request dto 클래스
@Getter
@RequiredArgsConstructor
public class CreateReviewRequestDto {

    @NotBlank
    private final int rating;

    @NotBlank
    private final String contents;
}
