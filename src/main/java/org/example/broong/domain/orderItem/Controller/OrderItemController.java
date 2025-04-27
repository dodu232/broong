package org.example.broong.domain.orderItem.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.orderItem.dto.request.AddToOrderItemRequestDto;
import org.example.broong.domain.orderItem.dto.response.UserOrderItemResponseDto;
import org.example.broong.domain.orderItem.entity.OrderItem;
import org.example.broong.domain.orderItem.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orderItems")
public class OrderItemController {

    private final OrderItemService userOrderItemService;

    @PostMapping("/orderItems")
    public ResponseEntity<UserOrderItemResponseDto> addOrderItem(@Auth AuthUser user, @Valid @RequestBody AddToOrderItemRequestDto dto) {
        return ResponseEntity.ok(userOrderItemService.addOrderItem(user.getId(), dto));
    }

    @GetMapping("/orderItems")
    public ResponseEntity<List<UserOrderItemResponseDto>> getOrderItem(@Auth AuthUser user) {
        List<OrderItem> orderItems = userOrderItemService.getOrderItem(user.getId());
        List<UserOrderItemResponseDto> response = orderItems.stream()
                .map(UserOrderItemResponseDto::from)
                .toList();
        return ResponseEntity.ok(response);
    }

}
