package org.example.broong.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.security.jwt.JwtService;
import org.springframework.security.core.Authentication;
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
            Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = extractCustomUserDetails(authentication);

        String accessToken = jwtService.generateAccessToken(customUserDetails.getUserId(), customUserDetails.getUsername(),customUserDetails.getUserType());
        String refreshToken = jwtService.generateRefreshToken( customUserDetails.getUsername());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        redisDao.setRefreshToken(customUserDetails.getUsername(),refreshToken.substring(7),refreshExpiration);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("로그인 성공.");

    }

    private CustomUserDetails extractCustomUserDetails(Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
