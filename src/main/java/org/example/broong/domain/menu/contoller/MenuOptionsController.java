package org.example.broong.domain.menu.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.menu.dto.request.MenuOptionsRequestDto;
import org.example.broong.domain.menu.dto.response.MenuOptionsResponseDto;
import org.example.broong.domain.menu.service.MenuOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuOptionsController {

    private final MenuOptionService menuOptionService;

    @PostMapping("/{menuId}/options")
    public ResponseEntity<MenuOptionsResponseDto> addMenuOption(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuOptionsRequestDto dto,
            @Auth AuthUser authUser) {

        MenuOptionsResponseDto response = menuOptionService.addMenuOption(storeId, menuId, dto, authUser.getId(), authUser.getUserType());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{menuId}/options/{optionId}")
    public ResponseEntity<MenuOptionsResponseDto> updateMenuOption(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @PathVariable Long optionId,
            @Valid @RequestBody MenuOptionsRequestDto dto,
            @Auth AuthUser authUser) {

        MenuOptionsResponseDto response = menuOptionService.updateMenuOption(storeId, menuId, optionId, dto, authUser.getId(), authUser.getUserType());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{menuId}/options/{optionId}")
    public ResponseEntity<Void> deleteMenuOption(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @PathVariable Long optionId,
            @Auth AuthUser authUser) {

        menuOptionService.deleteMenuOption(storeId, menuId, optionId, authUser.getId(), authUser.getUserType());
        return ResponseEntity.ok().build();
    }
}
