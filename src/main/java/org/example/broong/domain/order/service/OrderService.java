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

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto) {

        User user = userService.getById(userId);
        List<OrderItem> orderItems = orderItemService.getOrderItems(userId);
        if (orderItems.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "장바구니가 비어있습니다.");
        }

        // 가게 영업시간에만 주문 가능
        Store store = orderItems.get(0).getMenu().get();
        LocalTime now = LocalTime.now();
        if (now.isBefore(store.getOpeningTime()) || now.isAfter(store.getClosingTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                    "영업시간이 아닙니다.");
        }

        // 장바구니에 담긴 메뉴의 가격을 가져와서 총주문 금액 확인
    }


    public OrderStatusResponseDto cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "주문을 찾을 수 없습니다."));

        if (!ObjectUtils.nullSafeEquals(userId, order.getUser().getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "유저만 취소 할 수 있습니다.");
        }

        if (!OrderStatus.PENDING.equals(order.getOrderStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,"거절할 수 없습니다.");
        }

        order.updateOrderStatus(OrderStatus.REJECTED);

        return OrderStatusResponseDto.from(order);
    }


    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ErrorType.NO_RESOURCE,
                "주문을 찾을 수 없습니다."));
    }
}
