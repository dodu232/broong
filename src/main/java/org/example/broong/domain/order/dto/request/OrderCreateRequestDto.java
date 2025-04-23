package org.example.broong.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {

    @NotNull(message = "가게ID는 필수입니다.")
    private Long storeId;

    @NotNull(message = "주문ID는 필수입니다.")
    private Long orderId;

}
