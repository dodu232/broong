package org.example.broong.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponseDto {
    private Long id;             // 메뉴 고유 ID
    private Long storeId;        // 연결된 가게 ID
    //private String storeName;   //연관관계 시 추가 가능
    private String name;         // 메뉴 이름
    private String price;        // 가격
    private String menuState;    // 상태


}
