package org.example.broong.domain.orderItem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.orderItem.dto.response.UserOrderItemResponseDto;
import org.example.broong.domain.orderItem.service.OrderItemService;
import org.example.broong.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderItemController {

    private final OrderItemService userOrderItemService;

    @PostMapping("/orderItems")
    public ResponseEntity<UserOrderItemResponseDto> addOrderItem(@Auth AuthUser user, @Valid @RequestBody UserOrderItemResponseDto dto) {
        return ResponseEntity.ok(userOrderItemService.addOrderItem(user.getId(), dto));
    }

    @GetMapping("/orderItems")
    public ResponseEntity<List<User<UserOrderItemResponseDto>>> getOrderItem(@Auth AuthUser user) {
        return ResponseEntity.ok(userOrderItemService.getOrderItems(user.getId()));
    }

    @DeleteMapping("/orderItems/{menuId}")
    public ResponseEntity<Void> removeOrderItem(@Auth AuthUser user, @PathVariable Long menuId) {
        userOrderItemService.removeOrderItem(user.getId());
        return ResponseEntity.ok().build();
    }

}
