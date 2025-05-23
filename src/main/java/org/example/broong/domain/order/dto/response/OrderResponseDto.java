package org.example.broong.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private final Long id;
    private final Long storeId;
    private final Long menuId;
    private final int count;
    private final int totalPrice;
    private final LocalDateTime updatedAt;

}
