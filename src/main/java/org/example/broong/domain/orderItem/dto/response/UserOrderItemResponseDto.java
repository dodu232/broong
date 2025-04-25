package org.example.broong.domain.orderItem.dto.response;

import lombok.Getter;
import org.example.broong.domain.orderItem.entity.OrderItem;

@Getter
public class UserOrderItemResponseDto {

    private final Long id;
    private final int count;

    public UserOrderItemResponseDto(Long id, int count) {
        this.id = id;
        this.count = count;
    }

    public static UserOrderItemResponseDto from(OrderItem orderItem) {
        return new UserOrderItemResponseDto(orderItem.getId(), orderItem.getCount());
    }
}
