package org.example.broong.domain.order.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.dto.request.OrderCreateRequestDto;
import org.example.broong.domain.order.dto.response.OrderResponseDto;
import org.example.broong.domain.order.repository.OrderRepository;
import org.example.broong.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    @Transactional
    public OrderResponseDto createOrder(Long userId, @Valid OrderCreateRequestDto dto) {

        // 가게 영업시간에만 주문 가능

    }
}
