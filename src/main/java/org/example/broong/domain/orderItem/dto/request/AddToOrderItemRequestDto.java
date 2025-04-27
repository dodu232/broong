package org.example.broong.domain.orderItem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddToOrderItemRequestDto {

    @NotNull(message = "주문ID는 필수입니다.")
    private Long orderId;

    @NotNull(message = "메뉴옵션ID는 필수입니다.")
    private Long MenuOptionId;

    @Positive(message = "수량은 1개 이상이어야 합니다.")
    private int count;

}
