package org.example.broong.domain.store.service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
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
    private final UserService userService;

    public void addStore(StoreRequestDto.Add dto, long userId) {
        List<Store> findStores = storeRepository.findByUserId(userId);

        if (findStores.size() == 3) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                "가게는 3개까지 운영 가능합니다.");
        }

        Store saveStore = Store.builder()
            .name(dto.getName())
            .category(dto.getCategory())
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

    public List<StoreResponseDto.Get> getStoreListByUserId(long userId) {
        List<Store> findStore = storeRepository.findByUserId(userId);

        if (findStore.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                "보유 중인 가게가 없습니다.");
        }

        return findStore.stream()
            .map(e -> new StoreResponseDto.Get(
                e.getId(),
                e.getName(),
                e.getOpeningTime().toString(),
                e.getClosingTime().toString(),
                e.getMinOrderPrice()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateStore(Long storeId, long userId, StoreRequestDto.Update dto) {
        Store findStores = storeRepository.findById(storeId)
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                "해당하는 가게가 존재하지 않습니다."));

        /**
         * 로그인한 유저와 가게 주인의 id를 비교해 다르면 예외 처리
         */
        if (findStores.getUser().getId() != userId) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER,
                "해당 가게에 대한 수정 권한이 없습니다.");
        }

        findStores.updateStore(dto.getName(), dto.getCategory(), parseLocalTime(dto.getOpeningTime()),
            parseLocalTime(dto.getClosingTime()), dto.getMinOrderPrice());
    }

    @Transactional
    public void deleteStore(Long storeId, long userId){
        Store findStore = storeRepository.findById(storeId)
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                "해당하는 가게가 존재하지 않습니다."));

        if(findStore.getUser().getId() != userId){
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER,
                "해당 가게에 대한 삭제 권한이 없습니다.");
        }

        storeRepository.delete(findStore);
    }

    public Store findStore(Long storeId){
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                        "해당하는 가게가 존재하지 않습니다."));
    }

    // ReviewsService에서 store 조회용으로 필요해서 만들었습니다.
    public static LocalTime parseLocalTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, fmt);
    }
}
