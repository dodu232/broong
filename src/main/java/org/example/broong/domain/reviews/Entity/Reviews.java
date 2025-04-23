package org.example.broong.domain.reviews.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reviews")
public class Reviews extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private Users userId;

    @NotBlank
    @ManyToOne
    @JoinColumn(nullable = false, name = "store_id")
    private Stores storeId;

    @NotBlank
    @OneToOne
    @JoinColumn(nullable = false, name = "order_id")
    private Orders orderId;

    @NotBlank
    @Column(nullable = false)
    private int rating;

    @NotBlank
    @Column(nullable = false)
    private String contents;

    public Reviews(Users users, Orders orders, CreateReviewRequestDto createReviewRequestDto) {
        this.userId = users;
        this.orderId = orders;
        this.rating = createReviewRequestDto.getRating();
        this.contents = createReviewRequestDto.getContents();
    }

    public void update(UpdateReviewRequestDto updateReviewRequestDto) {
        this.rating = updateReviewRequestDto.getRating();
        this.contents = updateReviewRequestDto.getContents();
    }

    // todo baseEntity 상속 받아야함
    // todo 임의로 만든 User, Orders Entity를 바꿔야함
}