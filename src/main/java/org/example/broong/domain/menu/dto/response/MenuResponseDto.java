package org.example.broong.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;

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

    public static MenuResponseDto fromEntity(Menu menu) {
        return MenuResponseDto.builder()
                .id(menu.getId())
                .storeId(menu.getStore().getId())
                .storeName(menu.getStore().getName())
                .name(menu.getName())
                .price(menu.getPrice())
                .menuState(menu.getMenuState())
                .build();
    }

}
