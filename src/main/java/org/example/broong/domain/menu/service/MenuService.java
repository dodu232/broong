package org.example.broong.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.dto.request.MenuRequestDto;
import org.example.broong.domain.menu.dto.response.MenuResponseDto;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto createMenu(Store store, MenuRequestDto dto, Long userId, UserType userType) {

        if (userType != UserType.OWNER) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "사장님만 메뉴를 등록할 수 있습니다."
            );
        }

        if (!storeRepository.existsByIdAndUser_Id(store.getId(), userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "본인의 가게에만 메뉴를 등록할 수 있습니다."
            );
        }

        Menu menu = Menu.builder()
                .store(store)
                .name(dto.getName())
                .price(dto.getPrice())
                .menuState(MenuState.valueOf(dto.getMenuState()))
                .build();

        Menu saved = menuRepository.save(menu);

        return MenuResponseDto.builder()
                .id(saved.getId())
                .storeId(saved.getStore().getId())
                .storeName(store.getName())
                .name(saved.getName())
                .price(saved.getPrice())
                .menuState(saved.getMenuState())
                .build();
    }

    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId, MenuRequestDto dto, Long userId, UserType userType) {

        if (userType != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 메뉴를 수정할 수 있습니다.");
        }

        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "본인의 가게에만 메뉴를 수정할 수 있습니다."
            );
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "가게를 찾을 수 없습니다."));

        if (menu.getStore().getId() != storeId) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "해당 가게에 속한 메뉴가 아닙니다.");
        }

        menu.update(dto.getName(), dto.getPrice(), MenuState.valueOf(dto.getMenuState()));

        return MenuResponseDto.builder()
                .id(menu.getId())
                .storeId(menu.getStore().getId())
                .storeName(store.getName())
                .name(menu.getName())
                .price(menu.getPrice())
                .menuState(menu.getMenuState())
                .build();
    }

    @Transactional
    public void deleteMenu(Long storeId, Long menuId, Long userId, UserType userType) {

        if (userType != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 메뉴를 삭제할 수 있습니다.");
        }

        if (!storeRepository.existsByIdAndUser_Id(storeId, userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    ErrorType.INVALID_PARAMETER,
                    "본인의 가게에만 메뉴를 삭제할 수 있습니다."
            );
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        if (menu.getStore().getId() != storeId) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "해당 가게에 속한 메뉴가 아닙니다.");
        }

        menu.setMenuState(MenuState.DELETED);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getAllMenus() {
        List<Menu> menus = menuRepository.findAllByMenuStateNot(MenuState.DELETED);

        return menus.stream()
                .map(menu -> MenuResponseDto.builder()
                        .id(menu.getId())
                        .storeId(menu.getStore().getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .menuState(menu.getMenuState())
                        .build())
                .toList();
    }

}


