package org.example.broong.auto.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.user.enums.UserType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{8,}$" , message = "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    private String userType;

}
