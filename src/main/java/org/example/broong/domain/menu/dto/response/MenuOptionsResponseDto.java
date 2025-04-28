package org.example.broong.domain.menu.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.broong.domain.menu.entity.MenuOptions;

@Getter
@Builder
public class MenuOptionsResponseDto {

    private Long id;
    private String name;
    private int price;

    public static MenuOptionsResponseDto fromEntity(MenuOptions option) {
        return MenuOptionsResponseDto.builder()
                .id(option.getId())
                .name(option.getName())
                .price(option.getPrice())
                .build();
    }
}
