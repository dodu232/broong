package org.example.broong.domain.order.repository;

import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByMenuId(Long userId,Long menuId);
    List<OrderItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);

}
