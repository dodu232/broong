package org.example.broong.domain.menu.contoller;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.dto.MenuRequestDto;
import org.example.broong.domain.menu.dto.MenuResponseDto;
import org.example.broong.domain.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long storeId,
                                                      @RequestBody MenuRequestDto dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        MenuResponseDto response = menuService.createMenu(storeId, dto, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
