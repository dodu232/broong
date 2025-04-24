package org.example.broong.domain.order.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.order.Enum.OrderStatus;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {
    private final Long id;
    private final Long storeId;
    private final Integer totalPrice;


    public OrderResponseDto(Long id, Long storeId, Integer totalPrice) {
        this.id = id;
        this.storeId = storeId;
        this.totalPrice = totalPrice;

    }

}
