package org.example.broong.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.dto.MenuRequestDto;
import org.example.broong.domain.menu.dto.MenuResponseDto;
import org.example.broong.domain.menu.entity.Menus;
import org.example.broong.domain.menu.enums.MenuState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenusRepository menusRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto createMenu(Long storeId, MenuRequestDto dto, Long userId) {
        // 사장님 본인 가게인지 확인
        if (!storeRepository.existsByIdAndOwnerId(storeId, userId)) {
            throw new InvalidRequestException("본인의 가게에만 메뉴를 등록할 수 있습니다.");
        }

        Menus menu = Menus.builder()
                .storeId(storeId)
                .name(dto.getName())
                .price(Integer.parseInt(dto.getPrice())) // price는 숫자로 저장한다는 전제
                .menuState(MenuState.valueOf(dto.getMenuState()))
                .build();

        Menus saved = menusRepository.save(menu);

        return MenuResponseDto.builder()
                .id(saved.getId())
                .storeId(saved.getStoreId())
                .name(saved.getName())
                .price(String.valueOf(saved.getPrice()))
                .menuState(saved.getMenuState())
                .build();
    }

}
