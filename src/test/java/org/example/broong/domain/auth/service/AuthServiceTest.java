package org.example.broong.domain.auth.service;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.broong.domain.auth.dto.request.AuthRequestDto;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.global.exception.ApiException;
import org.example.broong.security.auth.RedisDao;
import org.example.broong.security.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisDao redisDao;

    @InjectMocks
    private AuthService autoService;

    @Test
    @DisplayName("존재하는 이메일로 회원가입 시도 시 예외 발생")
    void saveUser_whenUserExists() {

        // given
        String email = "exisiting@example.com";

        AuthRequestDto.Singup requestDto = new AuthRequestDto.Singup("user", email, "Password12", "USER");

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            autoService.signup(requestDto);
        });

        // then
        assertEquals("이미 존재하는 이메일 입니다.", exception.getMessage());

    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원가입 시도 시 성공")
    void saveUser_successSignup() {

        // given
        String email = "test1@example.com";

        AuthRequestDto.Singup requestDto = new AuthRequestDto.Singup("user", email, "Password12", "USER");

        given(userRepository.existsByEmail(email)).willReturn(false);

        // when & then
        assertDoesNotThrow(() -> autoService.signup(requestDto));

    }

    @Test
    @DisplayName("로그아웃 성공")
    void successLogout() {

        // given
        String accesstoken = "testAccessToken";
        String email = "aa@dd.com";
        Claims claims = mock(Claims.class);

        given(jwtService.substringToken(request, "access")).willReturn(Optional.of(accesstoken));
        given(jwtService.extractClaims(accesstoken, "access")).willReturn(claims);
        given(jwtService.extractEmail(claims)).willReturn(email);
        given(redisDao.hasKey(email)).willReturn(true);
        given(claims.getExpiration()).willReturn(new Date(System.currentTimeMillis() + 60000));

        // when & then
        assertDoesNotThrow(() -> autoService.logout(request));

    }

    @Test
    @DisplayName("엑세스 토큰이 없을 때")
    void notfoundAccessToken() {

        // given
        given(jwtService.substringToken(request, "access")).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            autoService.logout(request);
        });

        // then
        assertEquals("액세스 토큰이 존재하지 않습니다.", exception.getMessage());

    }

    @Test
    @DisplayName("리프레시 토큰이 없을 때")
    void notfoundRefreshToken() {

        // given
        String accesstoken = "testAccessToken";
        String email = "aa@dd.com";
        Claims claims = mock(Claims.class);

        given(jwtService.substringToken(request, "access")).willReturn(Optional.of(accesstoken));
        given(jwtService.extractClaims(accesstoken, "access")).willReturn(claims);
        given(jwtService.extractEmail(claims)).willReturn(email);
        given(redisDao.hasKey(email)).willReturn(false);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            autoService.logout(request);
        });

        // then
        assertEquals("이미 로그아웃한 유저입니다.", exception.getMessage());

    }

    @Test
    @DisplayName("액세스 토큰이 만료 됐을 때")
    void accessTokenAlreadyExpired() {

        // given
        String accesstoken = "testAccessToken";
        String email = "aa@dd.com";
        Claims claims = mock(Claims.class);

        given(jwtService.substringToken(request, "access")).willReturn(Optional.of(accesstoken));
        given(jwtService.extractClaims(accesstoken, "access")).willReturn(claims);
        given(jwtService.extractEmail(claims)).willReturn(email);
        given(redisDao.hasKey(email)).willReturn(true);
        given(claims.getExpiration()).willReturn(new Date(System.currentTimeMillis()));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            autoService.logout(request);
        });

        // then
        assertEquals("이미 만료된 액세스 토큰입니다.", exception.getMessage());
    }


}


