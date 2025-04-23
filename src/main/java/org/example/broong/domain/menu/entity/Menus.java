package org.example.broong.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.broong.domain.menu.enums.MenuState;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menus")
public class Menus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO: Stores 엔티티 생성 후 @ManyToOne 관계로 변경할 것
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "store_id", nullable = false)
    //private Stores stores;

    //Store entity가 없어 임시로 작성
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_state", nullable = false)
    private MenuState menuState;

}
