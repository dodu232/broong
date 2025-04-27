package org.example.broong.domain.order.dto.request;

import lombok.Getter;

public class PaymentRequest {
    @Getter
    public static class Confirm {
        private String paymentKey;
        private Long orderId;
        private int amount;
    }
}
