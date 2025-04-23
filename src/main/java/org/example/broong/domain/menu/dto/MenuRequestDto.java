package org.example.broong.domain.menu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequestDto {
    private Long storeId;        // 가게 식별자
    private String name;         // 메뉴 이름
    private String price;        // 가격 (문자열로 저장 중)
    private String menuState;    // 메뉴 상태 (예: AVAILABLE, DELETED 등)
}