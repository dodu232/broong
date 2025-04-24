package org.example.broong.domain.menu.contoller;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.service.MenuService;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final StoreRepository storeRepository;

    @PostMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long storeId,
                                                      @RequestBody MenuRequestDto dto/*,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails*/) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "가게를 찾을 수 없습니다."));

        MenuResponseDto response = menuService.createMenu(store, dto/*, userDetails.getUserId(), userDetails.getUserType()*/);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto dto/*,
            @AuthenticationPrincipal CustomUserDetails userDetails*/) {

        MenuResponseDto response = menuService.updateMenu(
                storeId,
                menuId,
                dto/*,
                userDetails.getUserId(),
                userDetails.getUserType()*/
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId/*,
            @AuthenticationPrincipal CustomUserDetails userDetails*/) {

        menuService.deleteMenu(storeId, menuId/*, userDetails.getUserId(), userDetails.getUserType()*/);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenus(@PathVariable Long storeId) {
        List<MenuResponseDto> menus = menuService.getMenusByStore(storeId);
        return ResponseEntity.ok(menus);
    }*/

}
