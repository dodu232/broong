package org.example.broong.domain.store.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository repository;

    public void addStore(StoreRequestDto.Add dto, long ownerId) {
        List<Store> list = repository.findByOwnerId(ownerId);

        if (list.size() == 3) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                "가게는 3개까지 운영 가능합니다.");
        }

        Store saveStore = Store.builder()
            .name(dto.getName())
            .category(dto.getCategory())
            .openingTime(parseLocalTime(dto.getOpeningTime()))
            .closingTime(parseLocalTime(dto.getClosingTime()))
            .minOrderPrice(dto.getMinOrderPrice())
            .build();

        repository.save(saveStore);
    }

    public static LocalTime parseLocalTime(String time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, fmt);
    }


}
