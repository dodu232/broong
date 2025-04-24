package org.example.broong.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.order.dto.response.OrderStatusResponseDto;
import org.example.broong.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderService orderService;

    @Transactional
    public OrderStatusResponseDto acceptOrder(Long ownerId, Long orderId) {

    }

}
