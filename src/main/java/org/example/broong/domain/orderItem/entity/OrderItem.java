package org.example.broong.domain.orderItem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.broong.domain.menu.entity.MenuOptions;
import org.example.broong.domain.order.entity.Order;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서는 생성자 생성 못하고 자기 패키지나 서브클래스에서만 생성 가능
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_option_id", nullable = false)
    private MenuOptions menuOption;

    @Column(nullable = false)
    private int count;

    public OrderItem(Order order, MenuOptions menuOption, int count) {
        this.order = order;
        this.menuOption = menuOption;
        this.count = count;
    }
}
