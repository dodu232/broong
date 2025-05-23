package org.example.broong.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.auth.service.AuthService;
import org.example.broong.domain.user.dto.DeleteUserRequestDto;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "DB에 존재하지 않는 유저입니다."));
    }

    @Transactional
    public void deleteUser(HttpServletRequest httpServletRequest, Long UserId, DeleteUserRequestDto requestDto) {

        User findUser = userRepository.findByIdOrElseThrow(UserId);

        if (passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            findUser.setDeletedAt(LocalDateTime.now());
            log.info("비밀번호 매칭 됨");
        } else {
            throw new ApiException(HttpStatus.UNAUTHORIZED, INVALID_PARAMETER, "잘못된 비밀번호 입니다.");
        }

        authService.logout(httpServletRequest);

    }
}
