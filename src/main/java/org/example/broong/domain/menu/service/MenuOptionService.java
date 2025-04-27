package org.example.broong.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.dto.request.MenuOptionsRequestDto;
import org.example.broong.domain.menu.dto.response.MenuOptionsResponseDto;
import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.entity.MenuOptions;
import org.example.broong.domain.menu.repository.MenuOptionsRepository;
import org.example.broong.domain.menu.repository.MenuRepository;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;
    private final MenuOptionsRepository menuOptionsRepository;

    @Transactional
    public MenuOptionsResponseDto addMenuOption(Long storeId, Long menuId, MenuOptionsRequestDto dto, Long userId, UserType userType) {
        if (userType != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 옵션을 추가할 수 있습니다.");
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        Store store = storeService.getMyStoreOrThrow(storeId, userId);

        if (menu.getStore().getId() != store.getId()) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "본인 가게의 메뉴만 수정할 수 있습니다.");
        }

        MenuOptions option = MenuOptions.builder()
                .menu(menu)
                .name(dto.getName())
                .price(dto.getPrice())
                .build();

        MenuOptions savedOption = menuOptionsRepository.save(option);

        return MenuOptionsResponseDto.fromEntity(savedOption);
    }

    @Transactional
    public MenuOptionsResponseDto updateMenuOption(Long storeId, Long menuId, Long optionId, MenuOptionsRequestDto dto, Long userId, UserType userType) {
        if (userType != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 옵션을 수정할 수 있습니다.");
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        Store store = storeService.getMyStoreOrThrow(storeId, userId);

        if (menu.getStore().getId() != store.getId()) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "본인 가게의 메뉴만 수정할 수 있습니다.");
        }

        MenuOptions option = menuOptionsRepository.findById(optionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "옵션을 찾을 수 없습니다."));

        option.update(dto.getName(), dto.getPrice());

        return MenuOptionsResponseDto.fromEntity(option);
    }

    @Transactional
    public void deleteMenuOption(Long storeId, Long menuId, Long optionId, Long userId, UserType userType) {
        if (userType != UserType.OWNER) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "사장님만 옵션을 삭제할 수 있습니다.");
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        Store store = storeService.getMyStoreOrThrow(storeId, userId);

        if (menu.getStore().getId() != store.getId()) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "본인 가게의 메뉴만 수정할 수 있습니다.");
        }

        MenuOptions option = menuOptionsRepository.findById(optionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "옵션을 찾을 수 없습니다."));

        menuOptionsRepository.delete(option);
    }
}
