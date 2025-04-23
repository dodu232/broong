package org.example.broong.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.broong.domain.menu.enums.MenuState;

@Getter
@AllArgsConstructor
@Builder
public class MenuResponseDto {
    private Long id;
    private Long storeId;
    // TODO : 추후 Store 연관관계 매핑 시 storeName도 포함 예정
    //private String storeName;
    private String name;
    private String price;
    private MenuState menuState;


}
