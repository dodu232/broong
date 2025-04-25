package org.example.broong.domain.reviews.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.store.entity.Stores;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
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
    private Stores storeId;

    @OneToOne
    @JoinColumn(nullable = false, name = "order_id")
    private Orders orderId;

    @NotNull
    @Column(nullable = false)
    private int rating;

    @NotBlank
    @Column(nullable = false)
    private String contents;

    public Reviews(User users, Orders orders, Stores store, CreateReviewRequestDto createReviewRequestDto) {
        this.userId = users;
        this.orderId = orders;
        this.storeId = store;
        this.rating = createReviewRequestDto.getRating();
        this.contents = createReviewRequestDto.getContents();
    }

    public void update(UpdateReviewRequestDto updateReviewRequestDto) {
        this.rating = updateReviewRequestDto.getRating();
        this.contents = updateReviewRequestDto.getContents();
    }
}