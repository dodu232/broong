package org.example.broong.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.config.PaymentProperties;
import org.example.broong.domain.order.dto.request.PaymentRequest;
import org.example.broong.domain.order.dto.response.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final WebClient webClient;
    private final PaymentProperties props;

    public PaymentResponse.Confirm confirmPayment(PaymentRequest.Confirm request){

        PaymentResponse.Confirm response = webClient.post()
                .uri(props.getConfirmEndpoint())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.Confirm.class)
                .block();

        return response;
    }
}
