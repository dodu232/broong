package org.example.broong.domain.order.repository;

import org.example.broong.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderRepository extends JpaRepository<Order, Long> {

}
