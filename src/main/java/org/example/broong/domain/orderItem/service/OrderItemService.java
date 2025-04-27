package org.example.broong.domain.orderItem.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.menu.entity.MenuOptions;
import org.example.broong.domain.menu.repository.MenuOptionRepository;
import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.order.repository.OrderRepository;
import org.example.broong.domain.orderItem.dto.request.AddToOrderItemRequestDto;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final UserService userService;

    @Transactional
    public UserOrderItemResponseDto addOrderItem(Long userId, AddToOrderItemRequestDto dto) {
        User user = userService.getById(userId);

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "주문을 찾을 수 없습니다."));

        MenuOptions menuOption = menuOptionRepository.findById(dto.getMenuOptionId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "메뉴 옵션을 찾을 수 없습니다."));

        OrderItem orderItem = new OrderItem(order, menuOption, dto.getCount());
        orderItemRepository.save(orderItem);

        return UserOrderItemResponseDto.from(orderItem);
    }

    public List<OrderItem> getOrderItem(Long userId) {
        return orderItemRepository.findByOrderUserId(userId);
    }
}
