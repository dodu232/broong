package org.example.broong.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.user.enums.UserType;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private int point = 0;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Builder
    public User(String email, String password, String name, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public void addPoint(int point) {
        this.point = this.point + point;
    }

}
