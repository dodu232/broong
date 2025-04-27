package org.example.broong.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;

    @Transactional
    public MenuResponseDto createMenu(Long storeId, MenuRequestDto dto, Long userId, UserType userType) {

        if (userType != UserType.OWNER) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "사장님만 메뉴를 등록할 수 있습니다."
            );
        }

        Store store = storeService.getStoreOwnedByUser(storeId, userId);

        Menu menu = Menu.builder()
                .store(store)
                .name(dto.getName())
                .price(dto.getPrice())
                .menuState(MenuState.valueOf(dto.getMenuState()))
                .build();

        Menu saved = menuRepository.save(menu);

        return MenuResponseDto.fromEntity(saved);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId, MenuRequestDto dto, UserType userType) {

        if (userType != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 메뉴를 수정할 수 있습니다.");
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        if (menu.getStore().getId() != storeId) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "해당 가게에 속한 메뉴가 아닙니다.");
        }

        menu.update(dto.getName(), dto.getPrice(), MenuState.valueOf(dto.getMenuState()));

        return MenuResponseDto.fromEntity(menu);
    }

    @Transactional
    public void deleteMenu(Long storeId, Long menuId, AuthUser authUser) {

        if (authUser.getUserType() != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 메뉴를 삭제할 수 있습니다.");
        }

        storeService.getStoreOwnedByUser(storeId, authUser.getId());

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        if (menu.getStore().getId() != storeId) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "해당 가게에 속한 메뉴가 아닙니다.");
        }

        menu.delete();
    }

    @Transactional(readOnly = true)
    public MenuResponseDto getMenuWithOptions(Long storeId, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        if (menu.getStore().getId() != storeId) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "본인 가게의 메뉴만 조회할 수 있습니다.");
        }

        if (menu.getMenuState() == MenuState.DELETED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "삭제된 메뉴는 조회할 수 없습니다.");
        }

        return MenuResponseDto.fromEntity(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMenusByStore(Long storeId, Long userId, UserType userType) {

        if (userType != UserType.OWNER) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "사장님만 메뉴를 조회할 수 있습니다."
            );
        }

        Store store = storeService.getStore(storeId);

        if (!store.getUser().getId().equals(userId)) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "본인의 가게만 조회할 수 있습니다."
            );
        }

        List<Menu> menus = menuRepository.findAllByStoreIdAndMenuState(storeId, MenuState.AVAILABLE);

        return menus.stream()
                .map(MenuResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}


