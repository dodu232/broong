package org.example.broong.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.order.service.OwnerOrderService;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class OwnerOrderController {

    private final OwnerOrderService ownerOrderService;

    @Order
    @PutMapping("/orders/{orderId}/accept")
    public ResponseEntity<OrderStatusResponseDto> acceptOrder(@Auth AuthUser owner, @PathVariable Long orderId) {
        return ResponseEntity.ok(ownerOrderService.acceptOrder(owner.getId(), orderId));
    }

    @Order
    @PutMapping("/orders/{orderId}/reject")
    public ResponseEntity<OrderStatusResponseDto> rejectOrder(@Auth AuthUser owner, @PathVariable Long orderId) {
        return ResponseEntity.ok(ownerOrderService.rejectOrder(owner.getId(), orderId));
    }
}
