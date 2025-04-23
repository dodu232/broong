package org.example.broong.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    private String menu_state;

}
