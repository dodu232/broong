package org.example.broong.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class PaymentResponse {

    @Getter
    public static class Confirm {
        private String status;
        @JsonProperty("paymentKey")
        private String paymentKey;
        @JsonProperty("orderId")
        private Long orderId;
        private int amount;
    }
}
