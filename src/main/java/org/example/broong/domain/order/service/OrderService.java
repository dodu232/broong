package org.example.broong.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.Enum.OrderStatus;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.order.repository.OrderRepository;
import org.example.broong.domain.orderItem.entity.OrderItem;
import org.example.broong.domain.orderItem.service.OrderItemService;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;
    private final UserService userService;

    // 사용자 주문 생성
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto) {

        User user = userService.getById(userId);
        List<OrderItem> orderItems = orderItemService.getOrderItems(userId);
        if (orderItems.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "오더 아이템이 비어있습니다.");
        }

        // 가게 영업시간에만 주문 가능
        Store store = orderItems.get(0).getMenu().getStore();
        LocalTime now = LocalTime.now();
        if (now.isBefore(store.getOpeningTime()) || now.isAfter(store.getClosingTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                    "영업시간이 아닙니다.");
        }

        // 가게에서 설정한 최소 주문 금액을 만족해야 주문이 가능

        // 장바구니에 담긴 메뉴의 가격을 가져와서 총주문 금액 확인

    }


    // 사용자 주문 취소
    @Transactional
    public OrderStatusResponseDto cancelOrder(Long userId, Long orderId) {
        Order order = getOrderById(orderId);

        validateUserOrder(order, userId);

        if (!OrderStatus.PENDING.equals(order.getOrderStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,"조리 중인 주문은 취소할 수 없습니다.");
        }

        order.updateOrderStatus(OrderStatus.CANCELED);

        return OrderStatusResponseDto.from(order);
    }

    // 오너 주문 상태 변경
    @Transactional
    public OrderStatusResponseDto changeOrderStatus(Long ownerId, Long orderId, OrderStatus newStatus ) {
        Order order = getOrderById(orderId);

        validateOwnerOrder(order, ownerId);

        if (!OrderStatus.PENDING.equals(order.getOrderStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,"변경할 수 없는 주문입니다.");
        }

        if (!isAllowedChangeStatus(newStatus)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "허용되지 않은 주문 상태입니다.");
        }

        order.updateOrderStatus(newStatus);
        return OrderStatusResponseDto.from(order);
    }

    private void validateUserOrder(Order order, Long userId) {
        if (!ObjectUtils.nullSafeEquals(userId, order.getStore().getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "주문한 유저가 아닙니다.");
        }
    }

    private void validateOwnerOrder(Order order, Long ownerId){
        if (!ObjectUtils.nullSafeEquals(ownerId, order.getStore().getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "오너만 주문 상태를 변경할 수 있습니다.");
        }
    }

    private boolean isAllowedChangeStatus(OrderStatus status) {
        return status == OrderStatus.ACCEPTED || status == OrderStatus.REJECTED;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE,
                        "주문을 찾을 수 없습니다."));
    }
}
