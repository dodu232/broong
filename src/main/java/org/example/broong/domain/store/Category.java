package org.example.broong.domain.store;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CAFE("카페"),
    PIZZA("피자"),
    FAST_FOOD("패스트푸드"),
    CHICKEN("치킨"),
    SNACK_BAR("분식"),
    CHINESE("중식"),
    KOREAN("한식"),
    JAPANESE("일식"),
    JJIM("찜"),
    BBQ("고기");

    private final String displayName;
}
