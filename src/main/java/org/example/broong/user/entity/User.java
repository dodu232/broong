package org.example.broong.user.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.common.entity.Timestamped;
import org.example.broong.user.enums.UserType;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

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

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    public User(String email, String password, String name, UserType userType){
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void deletedUser(){
        this.deletedAt = LocalDateTime.now();
    }

    public void addPoint(int point){
        this.point = this.point + point;
    }






}
