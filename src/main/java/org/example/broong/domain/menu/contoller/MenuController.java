package org.example.broong.domain.menu.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
            @Valid @PathVariable Long storeId,
            @RequestBody MenuRequestDto dto,
            @Auth AuthUser authUser){

        MenuResponseDto response = menuService.createMenu(storeId, dto, authUser.getId(), authUser.getUserType());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @Valid @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto dto,
            @Auth AuthUser authUser) {

        MenuResponseDto response = menuService.updateMenu(storeId, menuId, dto, authUser.getId(), authUser.getUserType());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @Valid @PathVariable Long storeId,
            @PathVariable Long menuId,
            @Auth AuthUser authUser) {

        menuService.deleteMenu(storeId, menuId, authUser);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenusByStore(
            @Valid @PathVariable Long storeId,
            @Auth AuthUser authUser) {
        List<MenuResponseDto> menus = menuService.getMenusByStore(
                storeId,
                authUser.getId(),
                authUser.getUserType()
        );
        return ResponseEntity.ok(menus);
    }

}
