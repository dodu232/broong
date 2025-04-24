package org.example.broong.domain.order.service;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.Enum.OrderStatus;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderService orderService;

    @Transactional
    public OrderStatusResponseDto acceptOrder(Long ownerId, Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!ObjectUtils.nullSafeEquals(ownerId, order.getStore().getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "오너가 아닙니다.");
        }

        if (!OrderStatus.PENDING.equals(order.getOrderStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,"취소할 수 없는 주문입니다.");
        }

        order.updateOrderStatus(OrderStatus.ACCEPTED);

        return OrderStatusResponseDto.from(order);

    }

    @Transactional
    public OrderStatusResponseDto rejectOrder(Long ownerId, Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!ObjectUtils.nullSafeEquals(ownerId, order.getStore().getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,"오너가 아닙니다.");
        }

        if (!OrderStatus.PENDING.equals(order.getOrderStatus())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,"거절할 수 없습니다.");
        }

        order.updateOrderStatus(OrderStatus.REJECTED);

        return OrderStatusResponseDto.from(order);
    }

}
