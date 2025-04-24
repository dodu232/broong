package org.example.broong.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequestDto {

    @NotNull(message = "storeId는 필수 입력 값입니다.")
    private Long storeId;

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "메뉴 이름은 2자 이상 30자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "가격은 필수입니다.")
    @Min(0)
    private int price;

    @NotBlank(message = "메뉴 상태는 필수입니다.")
    @Size(min = 2, max = 20, message = "메뉴 상태 2자 이상 30자 이하여야 합니다.")
    private String menuState;

}