package org.example.broong.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.entity.MenuOptions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class MenuResponseDto {
    private Long id;
    private Long storeId;
    private String storeName;
    private String name;
    private int price;
    private MenuState menuState;
    private int totalPrice;
    private List<MenuOptionsResponseDto> options;

    public static MenuResponseDto fromEntity(Menu menu) {
        List<MenuOptionsResponseDto> optionDtos = (menu.getMenuOptions() != null)
                ? menu.getMenuOptions().stream()
                .map(MenuOptionsResponseDto::fromEntity)
                .collect(Collectors.toList())
                : Collections.emptyList();

        int optionTotalPrice = (menu.getMenuOptions() != null)
                ? menu.getMenuOptions().stream()
                .mapToInt(MenuOptions::getPrice)
                .sum()
                : 0;

        int totalPrice = menu.getPrice() + optionTotalPrice;

        return MenuResponseDto.builder()
                .id(menu.getId())
                .storeId(menu.getStore() != null ? menu.getStore().getId() : null)
                .storeName(menu.getStore() != null ? menu.getStore().getName() : null)
                .name(menu.getName())
                .price(menu.getPrice())
                .menuState(menu.getMenuState() != null ? MenuState.valueOf(menu.getMenuState().name()) : null)
                .totalPrice(totalPrice) // <<=== 추가
                .options(optionDtos)
                .build();
    }

}
