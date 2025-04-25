package org.example.broong.domain.store.service.Menu.Service;

import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.service.MenuService;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

import static org.example.broong.domain.menu.enums.MenuState.AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("사장님이 아니면 메뉴생성 예외처리")
    void ownerCreateMenu() {
        // given
        Long storeId = 1L;
        Long userId = 100L;

        MenuRequestDto request = new MenuRequestDto("된장찌개", 9000, String.valueOf(AVAILABLE));

        User notOwner = User.builder()
                .name("손님")
                .userType(UserType.USER)
                .build();

        ReflectionTestUtils.setField(notOwner, "id", userId); // 테스트 전용 임시 설정


        Store store = Store.builder()
                .name("가게")
                .category(String.valueOf(Category.KOREAN))
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(21, 0))
                .minOrderPrice(10000)
                .user(notOwner)
                .build();

        ReflectionTestUtils.setField(store, "id", storeId);

        // when & then
        assertThrows(ApiException.class, () -> menuService.createMenu(store, request, userId, notOwner.getUserType()));
    }

    @Test
    @DisplayName("사장님이 아닌 사용자가 메뉴삭제 시도 예외처리")
    void UserMenuDelete() {
        // given
        Long storeId = 1L;
        Long menuId = 10L;
        Long userId = 100L;
        UserType userType = UserType.USER; // 일반 사용자

        // when & then
        ApiException exception = assertThrows(ApiException.class, () ->
                menuService.deleteMenu(storeId, menuId, userId, userType)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertEquals("사장님만 메뉴를 삭제할 수 있습니다.", exception.getMessage());
    }

}


