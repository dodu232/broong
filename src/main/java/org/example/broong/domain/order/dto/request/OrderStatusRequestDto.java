package org.example.broong.domain.order.dto.request;

import lombok.Getter;
import org.example.broong.domain.order.Enum.OrderStatus;

@Getter
public class OrderStatusRequestDto {

    private OrderStatus newStatus;
}
