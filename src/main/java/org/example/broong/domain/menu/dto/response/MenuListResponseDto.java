package org.example.broong.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuListResponseDto {
    private List<MenuResponseDto> menu;
    private int totalCount;
}
