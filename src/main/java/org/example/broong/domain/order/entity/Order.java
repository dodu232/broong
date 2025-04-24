package org.example.broong.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.order.Enum.OrderStatus;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.user.entity.User;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.http.HttpStatus;

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

    // 주문 요청 시각
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Order(User user, Store store, int totalPrice, OrderStatus orderStatus) {
        this.user = user;
        this.store = store;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.PENDING; // 기본값 설정
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if(this.orderStatus == orderStatus.CANCELED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER,
                    "취소된 주문은 상태 변경 할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }
}
