package org.example.broong.domain.order.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("주문 확인 전"),
    CANCELED("주문 취소"),
    ACCEPTED("주문 수락"),
    REJECTED("주문 거절");

    private final String description;
}
