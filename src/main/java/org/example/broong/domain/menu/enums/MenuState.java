package org.example.broong.domain.menu.enums;

import lombok.Getter;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;

@Getter
public enum MenuState {
    AVAILABLE,  //판매중
    HOLDOUT,    //품절
    DELETED;    //삭제

    public static MenuState of(String value) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                        INVALID_PARAMETER, "유효하지 않은 메뉴 상태입니다."));
    }

}
