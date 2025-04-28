package org.example.broong.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.broong.domain.order.enums.OrderStatus;
import org.example.broong.domain.order.entity.Order;

@Getter
@AllArgsConstructor
public class OrderStatusResponseDto {
    private final Long id;
    private final Long storeId;
    private final OrderStatus status;


    public static OrderStatusResponseDto from(Order order) {
        return new OrderStatusResponseDto(
                order.getId(),
                order.getStore().getId(),
                order.getOrderStatus()
        );
    }
}
