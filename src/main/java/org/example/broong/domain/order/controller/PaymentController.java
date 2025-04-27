package org.example.broong.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.dto.request.PaymentRequest;
import org.example.broong.domain.order.dto.response.PaymentResponse;
import org.example.broong.domain.order.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse.Confirm> confirm(
            @RequestBody PaymentRequest.Confirm request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.confirmPayment(request));
    }


}
