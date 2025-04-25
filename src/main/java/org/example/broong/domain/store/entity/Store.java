package org.example.broong.domain.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stores")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime openingTime;
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime closingTime;
    @Column(nullable = true)
    private int minOrderPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User user;

    @Builder
    public Store(String name, Category category, LocalTime openingTime, LocalTime closingTime,
        int minOrderPrice, User user) {
        this.name = name;
        this.category = category;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minOrderPrice = minOrderPrice;
        this.user = user;
    }

    public void updateStore(String name, Category category, LocalTime openingTime, LocalTime closingTime,
        Integer minOrderPrice){
        this.name =  name == null || name.isBlank() ? this.name : name;
        this.category = category == null ? this.category : category;
        this.openingTime = openingTime == null ? this.openingTime : openingTime;
        this.closingTime = closingTime == null ? this.closingTime : closingTime;
        this.minOrderPrice =  minOrderPrice == null ? this.minOrderPrice : minOrderPrice;
    }
}
