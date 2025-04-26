package org.example.broong.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.order.Enum.OrderStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequestDto {

    @NotNull
    private OrderStatus orderStatus;

}
