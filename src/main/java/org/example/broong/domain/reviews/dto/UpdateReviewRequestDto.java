package org.example.broong.domain.reviews.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 리뷰 업데이트 request dto 클래스
@Getter
@RequiredArgsConstructor
public class UpdateReviewRequestDto {

    @Digits(integer = 1,fraction = 0, message = "별점은 1, 2, 3, 4, 5만 입력할 수 있습니다.")
    @Min(1)
    @Max(5)
    @NotNull
    private final int rating;

    @NotBlank
    @Size(max = 255)
    private final String contents;
}
