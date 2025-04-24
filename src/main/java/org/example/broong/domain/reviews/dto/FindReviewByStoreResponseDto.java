package org.example.broong.domain.reviews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.example.broong.domain.reviews.Entity.Reviews;

import java.time.LocalDateTime;

// 리뷰 가게 기준 조회 response dto 클래스
@Getter
public class FindReviewByStoreResponseDto {

    private String userName;

    private int rating;

    private String contents;

    @JsonFormat(pattern = "mm-dd hh:mm")
    private LocalDateTime updatedAt;

    public FindReviewByStoreResponseDto(Reviews reviews) {
        this.userName = reviews.getUserId().getName();
        this.rating = reviews.getRating();
        this.contents = reviews.getContents();
        this.updatedAt = reviews.getUpdatedAt();
    }
}
