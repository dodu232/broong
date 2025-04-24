package org.example.broong.domain.common;

import lombok.Getter;
import org.example.broong.domain.user.enums.UserType;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserType userType;

    public AuthUser(Long id, String email, UserType userType) {
        this.id = id;
        this.email = email;
        this.userType = userType;
    }
}