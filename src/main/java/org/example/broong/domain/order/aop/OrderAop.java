package org.example.broong.domain.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.order.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OrderAop {

    private static final Logger logger = LoggerFactory.getLogger(OrderAop.class);

    // 주문 생성 성공 후 로깅
    @AfterReturning(pointcut = "execution(* org.example.broong.domain.order.service.OrderService.createOrder(..))", returning = "result")
    public void logCreateOrder(Object result) {
        if (result instanceof Order) {
            Order order = (Order) result;
            logger.info("[새로운 주문 생성완료] Order ID: {}, Store ID: {}, Total Price: {}, UpdatedAt:{}", order.getId(), order.getStore().getId(), order.getTotalPrice(), order.getUpdatedAt());
        }
    }

    // 주문 상태 변경 성공 후 로깅
    @AfterReturning(pointcut = "execution(* org.example.broong.domain.order.service.OrderService.changeOrderStatus(..))", returning = "result")
    public void logChangeOrderStatus(Object result) {
        if (result instanceof Order) {
            Order order = (Order) result;
            OrderStatus newStatus = order.getOrderStatus();
            logger.info("[주문 상태 변경 완료] Order ID: {}, Store ID: {}, New Status: {}", order.getId(), order.getStore().getId(), newStatus);
        }
    }
}
