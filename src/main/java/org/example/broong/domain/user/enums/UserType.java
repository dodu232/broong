package org.example.broong.domain.user.enums;

import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;

public enum UserType {
    USER, OWNER;

    public static UserType of(String type) {
        return Arrays.stream(UserType.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "유효하지 않은 UserType 입니다."));
    }


}
