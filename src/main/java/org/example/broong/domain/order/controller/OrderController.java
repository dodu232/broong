package org.example.broong.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.service.OrderService;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("orders")
    public ResponseEntity<OrderResponseDto> createOrder(@Auth User user, @Valid @RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(OrderService.createOrder(user.getId(),dto));
    }

    @PutMapping("orders/{orderId}/cancel")
    public ResponseEntity<OrderStatusResponseDto> cancelOrder(@Auth User user, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderService.cancelOrder(user.getId(), orderId));
    }
}
