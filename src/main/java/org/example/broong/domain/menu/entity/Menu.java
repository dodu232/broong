package org.example.broong.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.menu.enums.MenuState;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_state", nullable = false)
    private MenuState menuState;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOptions> menuOptions = new ArrayList<>();

    public void update(String name, int price, MenuState state) {
        this.name = name;
        this.price = price;
        this.menuState = state;
    }

    public void delete() {
        this.menuState = MenuState.DELETED;
    }


}
