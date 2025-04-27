package org.example.broong.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.order.enums.OrderStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequestDto {

    @NotNull(message = "변경할 주문 상태를 입력해주세요.")
    private OrderStatus orderStatus;

}
