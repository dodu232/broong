package org.example.broong.domain.testOrder;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.user.entity.User;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Store store;

    private int totalPrice;

    private String orderState;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Orders(User user, Store store, int totalPrice, String orderState) {
        this.user = user;
        this.store = store;
        this.totalPrice = totalPrice;
        this.orderState = orderState;
    }
}
