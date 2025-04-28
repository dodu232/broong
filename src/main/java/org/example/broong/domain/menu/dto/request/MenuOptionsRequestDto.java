package org.example.broong.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuOptionsRequestDto {

    @NotBlank(message = "옵션 이름은 필수입니다.")
    private String name;

    @Min(value = 0, message = "옵션 가격은 0원 이상이어야 합니다.")
    private int price;

    @Builder
    public MenuOptionsRequestDto(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
