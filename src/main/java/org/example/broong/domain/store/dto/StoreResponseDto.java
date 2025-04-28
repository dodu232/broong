package org.example.broong.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StoreResponseDto {

    @Getter
    @AllArgsConstructor
    public static class Get {
        private long id;
        private String name;
        private String openingTime;
        private String closingTime;
        private int minOrderPrice;
    }
}
