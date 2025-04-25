package org.example.broong.domain.menu.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuListResponseDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.service.MenuService;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

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
    public ResponseEntity<MenuListResponseDto> getAllMenus() {
        List<MenuResponseDto> menus = menuService.getAllMenus();
        return ResponseEntity.ok(new MenuListResponseDto(menus, menus.size()));
    }
}
