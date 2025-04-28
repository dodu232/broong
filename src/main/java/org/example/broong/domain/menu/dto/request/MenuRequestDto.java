package org.example.broong.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "메뉴 이름은 2자 이상 30자 이하여야 합니다.")
    private String name;

    @Min(0)
    private int price;

    @NotBlank(message = "메뉴 상태 필수입니다.")
    @Pattern(regexp = "AVAILABLE|HOLDOUT|DELETED", message = "유효하지 않은 메뉴 상태입니다.")
    private String menuState;

    @Builder.Default
    private List<MenuOptionsRequestDto> menuOptions = new ArrayList<>();

}