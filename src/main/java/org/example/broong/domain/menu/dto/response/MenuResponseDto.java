package org.example.broong.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.entity.MenuOptions;
import org.example.broong.domain.store.entity.Store;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        List<MenuOptionsResponseDto> optionDtos = Optional.ofNullable(menu.getMenuOptions())
                .orElse(Collections.emptyList())
                .stream()
                .map(MenuOptionsResponseDto::fromEntity)
                .collect(Collectors.toList());

        int optionTotalPrice = Optional.ofNullable(menu.getMenuOptions())
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(MenuOptions::getPrice)
                .sum();

        Long storeId = Optional.ofNullable(menu.getStore())
                .map(Store::getId)
                .orElse(null);

        String storeName = Optional.ofNullable(menu.getStore())
                .map(Store::getName)
                .orElse(null);

        MenuState menuState = Optional.ofNullable(menu.getMenuState())
                .map(state -> MenuState.valueOf(state.name()))
                .orElse(null);

        int totalPrice = menu.getPrice() + optionTotalPrice;

        return MenuResponseDto.builder()
                .id(menu.getId())
                .storeId(storeId)
                .storeName(storeName)
                .name(menu.getName())
                .price(menu.getPrice())
                .menuState(menuState)
                .totalPrice(totalPrice)
                .options(optionDtos)
                .build();
    }

}
