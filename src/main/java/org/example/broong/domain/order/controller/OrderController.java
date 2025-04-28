package org.example.broong.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.request.OrderStatusUpdateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.service.OrderService;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(customUserDetails.getUserId(),dto));
    }


    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderStatusResponseDto> cancelOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(customUserDetails.getUserId(), orderId));
    }


    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponseDto> changeOrderStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.changeOrderStatus(customUserDetails.getUserType(), orderId, dto.getOrderStatus()));
    }
}