package org.example.broong.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.security.jwt.JwtService;
import org.example.broong.security.RedisDao;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RedisDao redisDao;

    @Value("${refresh.expiration}")
    private long refreshExpiration;
    @Value("${access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        CustomUserDetails customUserDetails = extractCustomUserDetails(authentication);

        String accessToken = jwtService.generateAccessToken(customUserDetails.getUserId(), customUserDetails.getUsername(),customUserDetails.getUserType());
        String refreshToken = jwtService.generateRefreshToken( customUserDetails.getUsername());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        redisDao.setRefreshToken(customUserDetails.getUsername(),refreshToken,refreshExpiration);


        log.info("redis 조회 {}", redisDao.getRefreshToken(customUserDetails.getUsername()));
        log.info("로그인에 성공하였습니다. 이메일 : {}", customUserDetails.getUsername());
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    }

    private CustomUserDetails extractCustomUserDetails(Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
