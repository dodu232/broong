package org.example.broong.domain.reviews.repository;

import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewsCustomRepository {
    Slice<FindReviewByStoreResponseDto> findReviewListByStoreId(Long storeId, Pageable pageable);
}
