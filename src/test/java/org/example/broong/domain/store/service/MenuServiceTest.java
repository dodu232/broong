package org.example.broong.domain.store.service;

import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.menu.service.MenuService;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void 메뉴_생성_성공() {
        // given
        Long storeId = 1L;
        Long userId = 100L;
        String menuName = "양념치킨";
        int menuPrice = 10000;

        MenuRequestDto request = MenuRequestDto.builder()
                .storeId(storeId)
                .name(menuName)
                .price(menuPrice)
                .menuState("AVAILABLE")
                .build();

        Store store = Store.builder()
                .name("치킨집")
                .category(Category.KOREAN) // enum 가정
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(21, 0))
                .minOrderPrice(15000)
                .ownerId(userId)
                .build();

        when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> {
            Menu menu = invocation.getArgument(0);
            menu.setId(10L);
            return menu;
        });

        // when
        MenuResponseDto result = menuService.createMenu(store, request);

        // then
        assertEquals(menuName, result.getName());
        assertEquals(menuPrice, result.getPrice());
        assertEquals(MenuState.AVAILABLE, result.getMenuState());
    }
}



    /*@Test
    void 사장님이_아니면_메뉴_생성_예외() {
        // given
        Long storeId = 1L;
        Long userId = 100L;
        MenuRequestDto request = new MenuRequestDto("된장찌개", "9000", MenuState.AVAILABLE);
        User notOwner = new User(userId, "직원", UserType.USER);
        Store store = new Store(storeId, "가게", notOwner);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // when & then
        assertThrows(ApiException.class, () -> menuService.createMenu(storeId, request, userId));
    }*/


