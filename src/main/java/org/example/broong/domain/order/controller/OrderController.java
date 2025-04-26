package org.example.broong.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.request.OrderStatusUpdateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.service.OrderService;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Order
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@Auth AuthUser user, @Valid @RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(user.getId(), dto));
    }

    @Order
    @PatchMapping("/api/orders/{orderId}/cancel")
    public ResponseEntity<OrderStatusResponseDto> cancelOrder(@Auth AuthUser user, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(user.getId(), orderId));
    }

    @Order
    @PatchMapping("/api/orders/{orderId}/status")
    public ResponseEntity<OrderStatusResponseDto> changeOrderStatus(@Auth AuthUser owner, @PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.changeOrderStatus(owner.getId(), orderId, dto.getOrderStatus()));
    }
}
//    @Order
//    @PatchMapping("/api/orders/{orderId}/reject")
//    public ResponseEntity<OrderStatusResponseDto> rejectOrder(@Auth AuthUser owner, @PathVariable Long orderId) {
//        return ResponseEntity.ok(orderService.rejectOrder(owner.getId(), orderId));
//    }

