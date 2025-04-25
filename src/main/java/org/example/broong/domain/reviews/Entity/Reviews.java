package org.example.broong.domain.reviews.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.user.entity.User;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@SQLDelete(sql = "UPDATE broong.reviews SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
@Table(name = "reviews")
public class Reviews extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "store_id")
    private Store storeId;

    @OneToOne
    @JoinColumn(nullable = false, name = "order_id")
    private Orders orderId;

    @NotNull
    @Column(nullable = false)
    private int rating;

    @NotBlank
    @Column(nullable = false)
    private String contents;

    public Reviews(User users, Orders orders, Store store, int rating, String contents) {
        this.userId = users;
        this.orderId = orders;
        this.storeId = store;
        this.rating = rating;
        this.contents = contents;
    }

    public void update(int rating, String contents) {
        this.rating = rating;
        this.contents = contents;
    }
}