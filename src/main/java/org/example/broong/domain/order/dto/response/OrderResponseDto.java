package org.example.broong.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class OrderResponseDto {
    private final Long id;
    private final Long storeId;
    private final int totalPrice;
    private final LocalDateTime updatedAt;


}
