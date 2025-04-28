package org.example.broong.domain.order.service;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.order.enums.OrderStatus;
import org.example.broong.domain.order.repository.OrderRepository;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private OrderService orderService;

    private User user;
    private Store store;
    private Menu menu;
    private Order order;
    private OrderCreateRequestDto orderCreateRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .email("user@example.com")
                .password("password")
                .userType(UserType.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        store = Store.builder()
                .name("테스트 가게")
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(22, 0))
                .minOrderPrice(10000)
                .build();
        ReflectionTestUtils.setField(store, "id", 1L);

        menu = Menu.builder()
                .store(store)
                .name("교촌 레드 콤보")
                .price(21000)
                .menuState(null)
                .build();
        ReflectionTestUtils.setField(menu, "id", 1L);

        order = Order.builder()
                .user(user)
                .store(store)
                .menu(menu)
                .count(2)
                .totalPrice(42000)
                .orderStatus(OrderStatus.PENDING)
                .updatedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);

        orderCreateRequestDto = OrderCreateRequestDto.builder()
                .storeId(1L)
                .menuId(2L)
                .count(2)
                .build();
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_success() {
        // given
        given(userService.getById(1L)).willReturn(user);
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        OrderResponseDto result = orderService.createOrder(1L, orderCreateRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalPrice()).isEqualTo(16000);
    }

    @Test
    @DisplayName("주문 생성 실패 - 가게 없음")
    void createOrder_fail_store_not_found() {
        // given
        given(userService.getById(1L)).willReturn(user);
        given(storeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(1L, orderCreateRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("가게를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 생성 실패 - 메뉴 없음")
    void createOrder_fail_menu_not_found() {
        // given
        given(userService.getById(1L)).willReturn(user);
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(1L, orderCreateRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("메뉴를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 생성 실패 - 영업시간 아님")
    void createOrder_fail_not_open() {
        // given
        store = Store.builder()
                .name("닫힌 가게")
                .openingTime(LocalTime.of(23, 0))
                .closingTime(LocalTime.of(23, 59))
                .minOrderPrice(10000)
                .build();
        ReflectionTestUtils.setField(store, "id", 2L);

        given(userService.getById(1L)).willReturn(user);
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(1L, orderCreateRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("영업시간이 아닙니다.");
    }

    @Test
    @DisplayName("주문 생성 실패 - 최소주문금액 미달")
    void createOrder_fail_below_min_order_price() {
        // given
        menu = Menu.builder()
                .store(store)
                .name("교촌보다 비싼 치킨이 있을까")
                .price(24000)
                .menuState(null)
                .build();
        ReflectionTestUtils.setField(menu, "id", 2L);

        given(userService.getById(1L)).willReturn(user);
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(1L, orderCreateRequestDto))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("최소 주문 금액은");
    }

    @Test
    @DisplayName("주문 취소 성공")
    void cancelOrder_success() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        OrderStatusResponseDto result = orderService.cancelOrder(1L, 1L);

        // then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("주문 취소 실패 - 본인 주문 아님")
    void cancelOrder_fail_not_own_order() {
        // given
        User anotherUser = User.builder().email("another@example.com").password("pass").userType(UserType.USER).build();
        ReflectionTestUtils.setField(anotherUser, "id", 2L);

        order = Order.builder()
                .user(anotherUser)
                .store(store)
                .menu(menu)
                .count(2)
                .totalPrice(16000)
                .orderStatus(OrderStatus.PENDING)
                .updatedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.cancelOrder(1L, 1L))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("주문한 유저가 아닙니다.");
    }

    @Test
    @DisplayName("오더 상태 변경 성공 - ACCEPTED")
    void changeOrderStatus_success() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        OrderStatusResponseDto result = orderService.changeOrderStatus(UserType.OWNER, 1L, OrderStatus.ACCEPTED);

        // then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @Test
    @DisplayName("오더 상태 변경 실패 - OWNER 아님")
    void changeOrderStatus_fail_not_owner() {
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(UserType.USER, 1L, OrderStatus.ACCEPTED))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("오너만 주문 상태를 변경할 수 있습니다.");
    }

    @Test
    @DisplayName("오더 상태 변경 실패 - 허용되지 않은 상태")
    void changeOrderStatus_fail_invalid_status() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(UserType.OWNER, 1L, OrderStatus.CANCELED))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("허용되지 않은 주문 상태입니다.");
    }
}
