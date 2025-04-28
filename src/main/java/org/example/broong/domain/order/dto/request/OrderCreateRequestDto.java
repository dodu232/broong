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

    @NotNull(message = "메뉴ID는 필수입니다.")
    private Long menuId;

    @NotNull(message = "수량은 1개 이상이어야 합니다.")
    private Integer count;

}
