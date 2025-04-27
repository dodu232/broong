package org.example.broong.domain.auth.service;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.security.jwt.JwtService;
import org.example.broong.security.RedisDao;
import org.example.broong.domain.auth.dto.request.AuthRequestDto;
import org.example.broong.domain.user.enums.LoginType;
import org.example.broong.global.exception.ApiException;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisDao redisDao;
    private final JwtService jwtService;

    @Transactional
    public void signup(AuthRequestDto.Singup requestDto) {

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new ApiException(HttpStatus.CONFLICT, INVALID_PARAMETER, "이미 존재하는 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserType userType = UserType.of(requestDto.getUserType());
        LoginType loginType = LoginType.of(requestDto.getLoginType());

        User newUser = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .userType(userType)
                .loginType(loginType)
                .build();

        newUser.addPoint(100);

        userRepository.save(newUser);
        log.info("{}", userRepository.existsByEmail(requestDto.getEmail()));
    }

    public void logout(HttpServletRequest request) {
        String accessToken = jwtService.substringToken(request, "access").orElseThrow(
                () -> new ApiException(HttpStatus.BAD_REQUEST, NO_RESOURCE, "액세스 토큰이 존재하지 않습니다.")
        );

        if (!jwtService.isValidToken(accessToken, "access")) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, NO_RESOURCE, "유효하지 않은 액세스 토큰입니다.");
        }

        Claims claims = jwtService.extractClaims(accessToken, "access");

        // 현재 만료시간이 얼마큼 남았는지 확인
        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        long expireTime = (expiration.getTime() - now) / 1000; // 초 단위

        if (expireTime <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "이미 만료된 액세스 토큰입니다.");
        }


        redisDao.setBlackList(accessToken, "logout", expireTime);

        log.info("블랙리스트 {}",redisDao.getBlackList(accessToken));

        String email = jwtService.extractEmail(claims);

        if (redisDao.hasKey(email)) {
            redisDao.deleteRefreshToken(email);
        } else {
            throw new ApiException(HttpStatus.FORBIDDEN, INVALID_PARAMETER, "이미 로그아웃한 유저입니다.");
        }

        log.info("리프레시 삭제 여부 {}",redisDao.hasKey(email));

    }

}
