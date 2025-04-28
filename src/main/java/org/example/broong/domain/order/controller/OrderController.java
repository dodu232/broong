package org.example.broong.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.request.OrderStatusUpdateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Auth AuthUser user, @Valid @RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(user.getId(),dto));
    }


    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderStatusResponseDto> cancelOrder(@Auth AuthUser user, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(user.getId(), orderId));
    }


    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponseDto> changeOrderStatus(@Auth AuthUser owner, @PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.changeOrderStatus(owner.getId(), orderId, dto.getOrderStatus()));
    }
}