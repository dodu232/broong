package org.example.broong.domain.menu.service;

import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.example.broong.domain.menu.enums.MenuState.AVAILABLE;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private StoreService storeService;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    @Test
    @DisplayName("사장님은 정상적으로 메뉴를 등록할 수 있다.")
    void createMenu_Success() {
        // given
        Long storeId = 1L;
        Long userId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto("김치찌개", 8000, "AVAILABLE", null);

        //가게 생성
        Store store = Store.builder()
                .name("가게이름")
                .category(Category.BBQ)
                .openingTime(LocalTime.of(9,0))
                .closingTime(LocalTime.of(21,0))
                .minOrderPrice(13000)
                .build();

        ReflectionTestUtils.setField(store, "id", storeId);

        Menu menu = Menu.builder()
                .store(store)
                .name("김치찌개")
                .price(8000)
                .menuState(MenuState.AVAILABLE)
                .menuOptions(new ArrayList<>()) // <<< 반드시 빈 리스트라도 초기화!!
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        given(storeService.getStoreOwnedByUser(storeId, userId)).willReturn(store);
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponseDto response = menuService.createMenu(storeId, requestDto, userId, UserType.OWNER);

        // then
        assertThat(response.getName()).isEqualTo("김치찌개");
        assertThat(response.getPrice()).isEqualTo(8000);
        assertThat(response.getStoreId()).isEqualTo(storeId);
    }

    @Test
    @DisplayName("사장님이 아니면 메뉴 등록 시 예외가 발생한다.")
    void createMenu_NotOwner_ThrowsException() {
        // given
        Long storeId = 1L;
        Long userId = 1l;

        MenuRequestDto request = MenuRequestDto.builder()
                .name("된장찌개")
                .price(9000)
                .menuState("AVAILABLE")
                .build();

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(storeId, request, userId, UserType.USER))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("사장님만 메뉴를 등록할 수 있습니다.");
    }


    @Test
    @DisplayName("설명")
    void 테스트메서드명() {
        // given : 테스트 준비 (데이터 생성, 목 설정 등)
        // when : 테스트 대상 메서드 호출
        // then : 결과 검증
    }

    @Test
    @DisplayName("설명")
    void 테스트메서드명1() {
        // given : 테스트 준비 (데이터 생성, 목 설정 등)
        // when : 테스트 대상 메서드 호출
        // then : 결과 검증
    }

    @Test
    @DisplayName("설명")
    void 테스트메서드명2() {
        // given : 테스트 준비 (데이터 생성, 목 설정 등)
        // when : 테스트 대상 메서드 호출
        // then : 결과 검증
    }

}
