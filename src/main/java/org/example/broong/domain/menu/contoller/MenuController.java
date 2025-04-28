package org.example.broong.domain.menu.contoller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.service.MenuService;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
        @Valid @PathVariable Long storeId,
        @RequestBody MenuRequestDto dto,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        MenuResponseDto response = menuService.createMenu(storeId, dto, userDetails.getUserId(),
            userDetails.getUserType());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
        @Valid @PathVariable Long storeId,
        @PathVariable Long menuId,
        @RequestBody MenuRequestDto dto,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        MenuResponseDto response = menuService.updateMenu(storeId, menuId, dto,
            userDetails.getUserType());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
        @Valid @PathVariable Long storeId,
        @PathVariable Long menuId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        menuService.deleteMenu(storeId, menuId, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenuWithOptions(@PathVariable Long storeId,
        @PathVariable Long menuId) {
        MenuResponseDto menu = menuService.getMenuWithOptions(storeId, menuId);
        return ResponseEntity.ok(menu);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenusByStore(
        @Valid @PathVariable Long storeId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MenuResponseDto> menus = menuService.getMenusByStore(
            storeId,
            userDetails.getUserId(),
            userDetails.getUserType()
        );
        return ResponseEntity.ok(menus);
    }
}
