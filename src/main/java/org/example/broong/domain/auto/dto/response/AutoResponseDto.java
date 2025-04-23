package org.example.broong.domain.auto.dto.response;

import lombok.Getter;


public class AutoResponseDto {

    @Getter
    public static class Signup{
        private final String bearerToken;

        public Signup(String bearerToken){
            this.bearerToken = bearerToken;
        }
    }


}
