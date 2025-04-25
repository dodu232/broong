package org.example.broong.configsecurity;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/auth";
    private final JwtService jwtService;
    private final RedisDao redisDao;
    private final UserRepository userRepository;

    @Value("${refresh.expiration}")
    private long refreshExpiration;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.startsWith(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.substringToken(request, "refresh")
                .filter(token -> jwtService.isValidToken(token, "refresh"))
                .orElse(null);

        // refresh token 존재 -> access token 만료
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // refresh token 존재 안함 -> access token 만료 안됨
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }

    }

    // refresh token이 있으면 access token이 만료된 것 -> rtr방식에 따라 액세스 토큰 재발급과 동시에 리프레시 토큰도 재발급
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
            String refreshToken) {
        String email = jwtService.extractEmail(
                jwtService.extractClaims(refreshToken, "refresh"));
        userRepository.findByEmail(email).ifPresent(user -> {
            String reIssuedRefreshToken = reIssuedRefreshToken(email);
            jwtService.sendAccessAndRefreshToken(
                    response,
                    jwtService.generateAccessToken(
                            user.getId(),
                            user.getEmail(),
                            user.getUserType()),
                    reIssuedRefreshToken);
        });
    }

    // refresh token 재발급 및 redis에 업데이트
    private String reIssuedRefreshToken(String email) {
        String refreshToken = jwtService.generateRefreshToken(email);
        redisDao.setRefreshToken(email, refreshToken, refreshExpiration);
        return refreshToken;
    }


    //
    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        jwtService.substringToken(request, "access")
                .filter(accessToken -> jwtService.isValidToken(accessToken, "access"))
                .ifPresent(accessToken -> {
                    Claims claims = jwtService.extractClaims(accessToken, "access");
                    String email = jwtService.extractEmail(claims);
                    userRepository.findByEmail(email)
                            .ifPresent(this::saveAuthentication);
                });
        filterChain.doFilter(request, response);
    }




    public void saveAuthentication(User user){
        String password = user.getPassword();
        if(password == null){
            password = RandomUtil.RandowPassword();
        }


    }

}
