package org.example.broong.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.order.Enum.OrderStatus;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.user.entity.User;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    // 오더 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users", nullable = false)
    private User user;

    // 가게 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store", nullable = false)
    private Store store;

    // 총 가격
    @Column(nullable = false)
    private int totalPrice;

    // 주문 상태
    @Column(nullable = false)
    @Enumerated (EnumType.STRING)
    private OrderStatus orderStatus;

}
