package org.example.broong.domain.orderItem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.order.entity.Order;

import java.awt.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서는 생성자 생성 못하고 자기 패키지나 서브클래스에서만 생성 가능
@Table(name = "orderitems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_option_id", nullable = false)
    private MenuOption menuoption;


    @Column(nullable = false)
    private int count;

    public OrderItem(MenuOption menuoption, int count) {
        this.menuoption = menuoption;
        this.count = count;
    }

}
