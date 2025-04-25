package org.example.broong.domain.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.example.broong.domain.store.entity.QStore;
import org.example.broong.domain.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<StoreResponseDto.Get> findAllByCategory(Category category, Pageable pageable) {
        QStore store = QStore.store;

        List<Store> entities = queryFactory
            .selectFrom(store)
            .where(
                store.category.eq(category)
                    .and(store.deletedAt.isNull())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(store.createdAt.desc())
            .fetch();

        boolean hasNext = entities.size() > pageable.getPageSize();
        if (hasNext) {
            entities.remove(entities.size() - 1);
        }

        List<StoreResponseDto.Get> dtos = entities.stream()
            .map(e -> new StoreResponseDto.Get(
                e.getId(),
                e.getName(),
                e.getOpeningTime().toString(),
                e.getClosingTime().toString(),
                e.getMinOrderPrice()
            ))
            .collect(Collectors.toList());

        return new SliceImpl<>(dtos, pageable, hasNext);
    }
}
