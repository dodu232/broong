package org.example.broong.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class DeleteUserRequestDto {

    private final String password;

}
