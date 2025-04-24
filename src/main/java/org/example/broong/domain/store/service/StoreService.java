package org.example.broong.domain.store.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.store.repository.StoreRepositoryImpl;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreRepositoryImpl storeRepositoryImpl;
    private final UserService userService;

    public void addStore(StoreRequestDto.Add dto, long userId) {
        List<Store> findStores = storeRepository.findByUserId(userId);

        if (findStores.size() == 3) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                "가게는 3개까지 운영 가능합니다.");
        }

        Store saveStore = Store.builder()
            .name(dto.getName())
            .category(dto.getCategory().getDisplayName())
            .openingTime(parseLocalTime(dto.getOpeningTime()))
            .closingTime(parseLocalTime(dto.getClosingTime()))
            .minOrderPrice(dto.getMinOrderPrice())
            .user(userService.getById(userId))
            .build();

        storeRepository.save(saveStore);
    }

    public Slice<StoreResponseDto.Get> getStoreList(Category category, Pageable pageable) {
        return storeRepository.findAllByCategory(category, pageable);
    }

    public static LocalTime parseLocalTime(String time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, fmt);
    }
}
