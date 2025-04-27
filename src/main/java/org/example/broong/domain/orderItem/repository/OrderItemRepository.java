package org.example.broong.domain.orderItem.repository;

import org.example.broong.domain.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    // 유저 ID로 주문 아이템 조회
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.user.id = :userId")
    List<OrderItem> findByOrderUserId(@Param("userId") Long userId);
}
