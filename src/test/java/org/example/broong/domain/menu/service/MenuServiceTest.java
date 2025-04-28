package org.example.broong.domain.menu.service;

import org.example.broong.domain.menu.dto.request.MenuOptionsRequestDto;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.entity.MenuOptions;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.repository.MenuOptionsRepository;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.example.broong.security.auth.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class MenuAndMenuOptionServiceTest {

    @InjectMocks
    private MenuService menuService;

    @InjectMocks
    private MenuOptionService menuOptionService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuOptionsRepository menuOptionsRepository;

    @Mock
    private StoreService storeService;

    private Store store;
    private Menu menu;
    private MenuOptions menuOption;
    private MenuRequestDto menuRequestDto;
    private MenuOptionsRequestDto menuOptionsRequestDto;
    private CustomUserDetails ownerUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        store = Store.builder()
                .name("테스트 가게")
                .category(null)
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(22, 0))
                .minOrderPrice(10000)
                .build();
        ReflectionTestUtils.setField(store, "id", 1L);

        menu = Menu.builder()
                .store(store)
                .name("김치찌개")
                .price(8000)
                .menuState(MenuState.AVAILABLE)
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        menuOption = MenuOptions.builder()
                .menu(menu)
                .name("곱빼기")
                .price(2000)
                .build();
        ReflectionTestUtils.setField(menuOption, "id", 1L);

        menuRequestDto = MenuRequestDto.builder()
                .name("된장찌개")
                .price(8500)
                .menuState("AVAILABLE")
                .build();

        menuOptionsRequestDto = MenuOptionsRequestDto.builder()
                .name("곱빼기")
                .price(2000)
                .build();

        ownerUserDetails = new CustomUserDetails(
                1L,
                "owner@example.com",
                "1234",
                UserType.OWNER,
                null,
                List.of());
    }

    // -------------------- MenuService 테스트 --------------------

    @Test
    @DisplayName("메뉴 생성 성공")
    void createMenu_success() {
        // given
        given(storeService.getStoreOwnedByUser(1L, 1L)).willReturn(store);
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        var result = menuService.createMenu(1L, menuRequestDto, 1L, UserType.OWNER);

        // then
        assertThat(result.getName()).isEqualTo("김치찌개");
    }

    @Test
    @DisplayName("메뉴 생성 실패 - 사장님이 아님")
    void createMenu_fail_not_owner() {
        // when & then
        assertThatThrownBy(() -> menuService.createMenu(1L, menuRequestDto, 1L, UserType.USER))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("메뉴 수정 성공")
    void updateMenu_success() {
        // given
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        MenuRequestDto updateDto = MenuRequestDto.builder()
                .name("수정된 김치찌개")
                .price(9000)
                .menuState("AVAILABLE")
                .build();

        // when
        var result = menuService.updateMenu(1L, 1L, updateDto, UserType.OWNER);

        // then
        assertThat(result.getName()).isEqualTo("수정된 김치찌개");
        assertThat(result.getPrice()).isEqualTo(9000);
    }

    @Test
    @DisplayName("메뉴 삭제 성공")
    void deleteMenu_success() {
        // given
        given(storeService.getStoreOwnedByUser(1L, 1L)).willReturn(store);
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        // when
        menuService.deleteMenu(1L, 1L, ownerUserDetails);

        // then
        assertThat(menu.getMenuState()).isEqualTo(MenuState.DELETED);
    }

    @Test
    @DisplayName("단일 메뉴 조회 성공")
    void getMenuWithOptions_success() {
        // given
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        // when
        var result = menuService.getMenuWithOptions(1L, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("김치찌개");
    }

    @Test
    @DisplayName("가게 메뉴 목록 조회 성공")
    void getMenusByStore_success() {
        // given
        given(storeService.getStore(1L)).willReturn(store);

        User user = new User();  // User 객체 생성
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(store, "user", user);

        given(menuRepository.findAllByStoreIdAndMenuState(1L, MenuState.AVAILABLE)).willReturn(List.of(menu));

        // when
        var result = menuService.getMenusByStore(1L, 1L, UserType.OWNER);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("김치찌개");
    }

    // -------------------- MenuOptionService 테스트 --------------------

    @Test
    @DisplayName("메뉴 옵션 추가 성공")
    void addMenuOption_success() {
        // given
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        given(storeService.getMyStoreOrThrow(1L, 1L)).willReturn(store);
        given(menuOptionsRepository.save(any(MenuOptions.class))).willReturn(menuOption);

        // when
        var result = menuOptionService.addMenuOption(1L, 1L, menuOptionsRequestDto, 1L, UserType.OWNER);

        // then
        assertThat(result.getName()).isEqualTo("곱빼기");
    }

    @Test
    @DisplayName("메뉴 옵션 추가 실패 - 사장님이 아님")
    void addMenuOption_fail_not_owner() {
        // when & then
        assertThatThrownBy(() -> menuOptionService.addMenuOption(1L, 1L, menuOptionsRequestDto, 1L, UserType.USER))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("메뉴 옵션 수정 성공")
    void updateMenuOption_success() {
        // given
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        given(storeService.getMyStoreOrThrow(1L, 1L)).willReturn(store);
        given(menuOptionsRepository.findById(1L)).willReturn(Optional.of(menuOption));

        MenuOptionsRequestDto updateDto = MenuOptionsRequestDto.builder()
                .name("수정된 곱빼기")
                .price(2500)
                .build();

        // when
        var result = menuOptionService.updateMenuOption(1L, 1L, 1L, updateDto, 1L, UserType.OWNER);

        // then
        assertThat(result.getName()).isEqualTo("수정된 곱빼기");
        assertThat(result.getPrice()).isEqualTo(2500);
    }

    @Test
    @DisplayName("메뉴 옵션 삭제 성공")
    void deleteMenuOption_success() {
        // given
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        given(storeService.getMyStoreOrThrow(1L, 1L)).willReturn(store);
        given(menuOptionsRepository.findById(1L)).willReturn(Optional.of(menuOption));

        // when
        menuOptionService.deleteMenuOption(1L, 1L, 1L, 1L, UserType.OWNER);

        // then
        then(menuOptionsRepository).should().delete(menuOption);
    }

}
