package org.example.broong.domain.order.dto.response;

import lombok.Getter;
import org.example.broong.domain.order.Enum.OrderStatus;
import org.example.broong.domain.order.entity.Order;

@Getter
public class OrderStatusResponseDto {
    private final Long id;
    private final Long storeId;
    private final OrderStatus status;

    public OrderStatusResponseDto(Long id, Long storeId, OrderStatus status) {
        this.id = id;
        this.storeId = storeId;
        this.status = status;
    }

    public static OrderStatusResponseDto from(Order order) {
        return new OrderStatusResponseDto(
                order.getId(),
                order.getStore().getId(),
                order.getOrderStatus()
        );
    }
}
