package org.example.broong.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthResponseDto {
    private final String bearerToken;

    public AuthResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
