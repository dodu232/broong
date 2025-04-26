package org.example.broong.domain.orderItem.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.repository.OrderRepository;
import org.example.broong.domain.orderItem.dto.response.UserOrderItemResponseDto;
import org.example.broong.domain.orderItem.entity.OrderItem;
import org.example.broong.domain.orderItem.repository.OrderItemRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final MenuRepoisitory menuRepository;
    private final UserService userService;

    @Transactional
    public UserOrderItemResponseDto addOrderItem(Long userId, @Valid UserOrderItemResponseDto dto) {
        User user = userService.getById(userId);

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "메뉴를 찾을 수 없습니다."));

        OrderItem orderItem = OrderItemRepository.findById(userId, dto.getMenuId()).map(existingOrderItem -> {

        })
    }

    public List<OrderItem> getOrderItems(Long userId) {
        return OrderRepository.findByUserId(userId);
    }

}
