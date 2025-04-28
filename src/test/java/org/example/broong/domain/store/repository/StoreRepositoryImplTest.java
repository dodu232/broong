package org.example.broong.domain.store.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.QStore;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class StoreRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<Store> storeQuery;

    @InjectMocks
    private StoreRepositoryImpl storeRepositoryImpl;

    @Test
    @DisplayName("queryFactory로 Store list 를 불러온다.")
    void findAllByCategory() {
        // given
        QStore store = QStore.store;
        Pageable pageable = PageRequest.of(0, 10);
        Category category = Category.FAST_FOOD;

        List<User> dummyUser = IntStream.rangeClosed(0, 10)
            .mapToObj(i -> User.builder()
                .email(i + "test@email.com")
                .password(i + "password")
                .name("name" + i)
                .userType(UserType.OWNER)
                .build()
            )
            .toList();

        List<Store> dummyStores = IntStream.rangeClosed(0, 10)
            .mapToObj(i -> Store.builder()
                .name("store" + i)
                .category(category)
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(21, 0))
                .minOrderPrice(10000)
                .user(dummyUser.get(i))
                .build()
            )
            .toList();

        given(queryFactory.selectFrom(store))
            .willReturn(storeQuery);
        given(storeQuery.where(any(Predicate.class)))
            .willReturn(storeQuery);
        given(storeQuery.offset(anyLong()))
            .willReturn(storeQuery);
        given(storeQuery.limit(anyLong()))
            .willReturn(storeQuery);
        given(storeQuery.orderBy(any(OrderSpecifier.class)))
            .willReturn(storeQuery);
        given(storeQuery.fetch())
            .willReturn(dummyStores);

        // when
        List<Store> result = queryFactory
            .selectFrom(store)
            .where(
                store.category.eq(category)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(store.createdAt.desc())
            .fetch();

        // then
        assertThat(result).hasSize(dummyStores.size());
        verify(queryFactory, times(1)).selectFrom(any());
        verify(storeQuery, times(1)).where(store.category.eq(category));
        verify(storeQuery, times(1)).offset(pageable.getOffset());
        verify(storeQuery, times(1)).limit(pageable.getPageSize() + 1);
        verify(storeQuery, times(1)).orderBy(store.createdAt.desc());
        verify(storeQuery, times(1)).fetch();
    }
}
