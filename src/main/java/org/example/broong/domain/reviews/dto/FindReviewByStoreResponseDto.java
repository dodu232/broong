package org.example.broong.domain.reviews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.broong.domain.reviews.Entity.Reviews;

import java.time.LocalDateTime;

// 리뷰 가게 기준 조회 response dto 클래스
@Getter
@AllArgsConstructor
public class FindReviewByStoreResponseDto {

    private String userName;
    private String storeName;

    private Long orderId;

    private int rating;

    private String contents;

    @JsonFormat(pattern = "MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
