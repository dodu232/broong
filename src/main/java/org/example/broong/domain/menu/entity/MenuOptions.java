package org.example.broong.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.broong.domain.menu.entity.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "menu_options")
public class MenuOptions {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "menu_id", nullable = false)
        private Menu menu;

        @Column(nullable = false, length = 30)
        private String name;

        @Column(nullable = false)
        private int price;

}
