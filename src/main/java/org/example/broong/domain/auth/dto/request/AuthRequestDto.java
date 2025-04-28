package org.example.broong.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDto {

    // 회원가입
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Singup {

        @NotBlank
        private String name;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{8,50}$", message = "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.")
        private String password;

        @NotBlank
        private String userType;

    }

}
