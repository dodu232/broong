package org.example.broong.domain.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.entity.Stores;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("가게를 3개 등록한 상태면 api 예외 호출")
    void checkStoreCount(){
        // given
        long userId = 1L;

        StoreRequestDto.Add dto = new StoreRequestDto.Add(
            "새가게",
            Category.FAST_FOOD,
            "09:00",
            "21:00",
            10000
        );

        List<Stores> dummyStores = IntStream.rangeClosed(1, 3)
            .mapToObj(i -> Stores.builder()
                .name("store" + i)
                .category(Category.FAST_FOOD)
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(21, 0))
                .minOrderPrice(10000)
                .user(userService.getById(userId))
                .build()
            )
            .collect(Collectors.toList());

        given(storeRepository.findByUserId(userId)).willReturn(dummyStores);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> storeService.addStore(dto, userId));
        verify(storeRepository, times(1)).findByUserId(anyLong());
        assertEquals("가게는 3개까지 운영 가능합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("가게가 정상적으로 등록 된다.")
    void addStore(){
        // given
        long ownerId = 1;
        StoreRequestDto.Add dto = new StoreRequestDto.Add(
            "새가게",
            Category.FAST_FOOD,
            "09:00",
            "21:00",
            10000
        );

        // when
        storeService.addStore(dto, ownerId);

        // then
        verify(storeRepository, times(1)).save(any());
    }
}
