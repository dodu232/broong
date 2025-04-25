package org.example.broong.domain.menu.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class MenuRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "메뉴 이름은 2자 이상 30자 이하여야 합니다.")
    private String name;

    @Min(0)
    private int price;

    @Pattern(regexp = "AVAILABLE|HOLDOUT|DELETED", message = "유효하지 않은 메뉴 상태입니다.")
    private String menuState;

    public MenuRequestDto(String name, int price, String menuState) {
        this.name = name;
        this.price = price;
        this.menuState = menuState;
    }
}