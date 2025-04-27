package org.example.broong.domain.user.enums;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;

import java.util.Arrays;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;

public enum LoginType {
    EMAIL, SOSIAL;

    public static LoginType of(String type){
        return Arrays.stream(LoginType.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "유효하지 않은 LoginType 입니다." ));
    }



}
