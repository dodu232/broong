package org.example.broong.domain.store.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;

    public void addStore(StoreRequestDto.Add dto, long userId) {
        List<Store> findStores = storeRepository.findByUserId(userId);

        if (findStores.size() == 3) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER,
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

    public List<StoreResponseDto.Get> getStoreListByUserId(long userId){
        List<Store> findStore = storeRepository.findByUserId(userId);

        if(findStore.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "보유 중인 가게가 없습니다.");
        }

        return  findStore.stream()
            .map(e -> new StoreResponseDto.Get(
                e.getName(),
                e.getOpeningTime().toString(),
                e.getClosingTime().toString(),
                e.getMinOrderPrice()
            ))
            .collect(Collectors.toList());
    }

    public static LocalTime parseLocalTime(String time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, fmt);
    }

    @Transactional
    public Store getMyStoreOrThrow(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "가게를 찾을 수 없습니다."));

        if (!store.getUser().getId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "본인의 가게만 접근할 수 있습니다.");
        }

        return store;
    }

    @Transactional
    public Store getStoreOwnedByUser(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "가게를 찾을 수 없습니다."));

        if (!store.getUser().getId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "본인의 가게만 접근할 수 있습니다.");
        }

        return store;
    }

}
