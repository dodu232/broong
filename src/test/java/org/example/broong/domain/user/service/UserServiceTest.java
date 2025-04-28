package org.example.broong.domain.user.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import org.example.broong.domain.auth.service.AuthService;
import org.example.broong.domain.user.dto.DeleteUserRequestDto;
import org.example.broong.domain.user.enums.UserType;
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
import org.example.broong.domain.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JwtService jwtService;

    @Mock
    private RedisDao redisDao;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("찾는 유저가 존재하는 경우")
    void successFindByUser(){
        Long userId= 1L;

        String encodedPassword = passwordEncoder.encode("Test1234");

        User newUser = User.builder()
                .name("user")
                .email("ee@ee.com")
                .password(encodedPassword)
                .userType(UserType.USER)
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(newUser));

        assertDoesNotThrow(() -> userService.getById(userId));
    }

    @Test
    @DisplayName("찾는 유저가 존재하지 않는 경우")
    void failFindUserWhenNotFound(){
        // given
        Long userId= 1L;

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.getById(userId);
        });

        // then
        assertEquals("DB에 존재하지 않는 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void successDeleteUser(){

        // given
        String accesstoken = "testAccessToken";
        String email = "aa@dd.com";
        Claims claims = mock(Claims.class);
        Long userId= 1L;

        String rawPassword = "Test1234";
        String encodedPassword = passwordEncoder.encode("Test1234");

        DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto(rawPassword);

        User newUser = User.builder()
                .name("user")
                .email("ee@ee.com")
                .password(encodedPassword)
                .userType(UserType.USER)
                .build();

        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(newUser);
        given(passwordEncoder.matches(rawPassword, newUser.getPassword())).willReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(request, userId, deleteUserRequestDto));
    }


    @Test
    @DisplayName("회원 탈퇴하려는데 비밀번호 안맞음")
    void failDeleteUserWhenPasswordMismatch(){

        // given
        String accesstoken = "testAccessToken";
        String email = "aa@dd.com";
        Claims claims = mock(Claims.class);
        Long userId= 1L;

        String rawPassword = "Test1234";
        String encodedPassword = passwordEncoder.encode("Test1234");

        DeleteUserRequestDto deleteUserRequestDto = new DeleteUserRequestDto(rawPassword);

        User newUser = User.builder()
                .name("user")
                .email("ee@ee.com")
                .password(encodedPassword)
                .userType(UserType.USER)
                .build();

        given(userRepository.findByIdOrElseThrow(anyLong())).willReturn(newUser);
        given(passwordEncoder.matches(rawPassword, newUser.getPassword())).willReturn(false);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.deleteUser(request,userId,deleteUserRequestDto);
        });

        // then
        assertEquals("잘못된 비밀번호 입니다.", exception.getMessage());
    }

}
