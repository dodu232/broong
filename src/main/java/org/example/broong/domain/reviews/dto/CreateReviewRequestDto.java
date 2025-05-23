package org.example.broong.domain.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 리뷰 생성 request dto 클래스
@Getter
@RequiredArgsConstructor
public class CreateReviewRequestDto {

    @Min(value = 1, message = "별점은 1, 2, 3, 4, 5만 입력할 수 있습니다.")
    @Max(value = 5, message = "별점은 1, 2, 3, 4, 5만 입력할 수 있습니다.")
    private final int rating;

    @NotBlank(message = "리뷰를 입력해주세요")
    @Size(max = 255, message = "255 글자까지 입력할 수 있습니다.")
    private final String contents;
}
