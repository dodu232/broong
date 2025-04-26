package org.example.broong.domain.reviews.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.reviews.Entity.QReviews;
import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.FindReviewByStoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewsCustomRepositoryImpl implements ReviewsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<FindReviewByStoreResponseDto> findReviewListByStoreId(Long storeId, Pageable pageable) {
        QReviews qreviews = QReviews.reviews;

        List<Reviews> reviews = jpaQueryFactory
                .selectFrom(qreviews)
                .where(
                        qreviews.storeId.id.eq(storeId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(qreviews.updatedAt.desc())
                .fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();
        if (hasNext) {
            reviews.remove(reviews.size() - 1);
        }

        List<FindReviewByStoreResponseDto> responseDtoList = reviews.stream()
                .map(r -> new FindReviewByStoreResponseDto(
                        r.getUserId().getName(),
                        r.getStoreId().getName(),
                        r.getOrderId().getId(),
                        r.getRating(),
                        r.getContents(),
                        r.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        return new SliceImpl<>(responseDtoList, pageable, hasNext);
    }
}
