package org.example.broong.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.order.Enum.OrderStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class OrderResponseDto {
    private final Long id;
    private final Long storeId;
    private final int totalPrice;
    private final LocalDateTime updatedAt;


}
