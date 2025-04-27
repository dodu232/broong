package org.example.broong.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.common.BaseEntity;
import org.example.broong.domain.user.enums.LoginType;
import org.example.broong.domain.user.enums.UserType;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private int point = 0;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Builder
    public User(String email, String password, String name, UserType userType, LoginType loginType){
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.loginType = loginType;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void addPoint(int point){
        this.point = this.point + point;
    }






}
