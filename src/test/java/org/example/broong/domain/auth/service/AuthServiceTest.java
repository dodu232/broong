package org.example.broong.domain.auth.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import org.example.broong.domain.auth.dto.request.AuthRequestDto;
import org.example.broong.global.exception.ApiException;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.security.auth.RedisDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

//    @Mock
//    private RedisDao redisDao;
//
//    @Mock
//    jw

    @InjectMocks
    private AuthService autoService;

    @Test
    @DisplayName("존재하는 이메일로 회원가입 시도 시 예외 발생")
    void saveUser_whenUserExists(){
        // given
        String email = "exisiting@example.com";

        AuthRequestDto.Singup requestDto = new AuthRequestDto.Singup("user" , email , "Password12","USER");

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            autoService.signup(requestDto);
        });

        // then
        assertEquals("이미 존재하는 이메일 입니다.", exception.getMessage());

    }

//    @Test
//    @DisplayName("존재하지 않는 이메일로 회원가입 시도 시 성공")
//    void saveUser_successSignin(){
//        // given
//        String email = "test1@example.com";
//
//        AuthRequestDto.Singup requestDto = new AuthRequestDto.Singup("user" , email , "Password12","USER");
//
//        given(userRepository.existsByEmail(email)).willReturn(false);
//
//        // when
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            autoService.signup(requestDto);
//        });
//
//        // then
//        assertEquals("이미 존재하는 이메일 입니다.", exception.getMessage());
//
//    }

}


