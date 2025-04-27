package org.example.broong.security.jwt;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NOT_LOGGED_IN;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.global.exception.ApiException;
import org.example.broong.security.auth.RedisDao;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/auth";
    private final JwtService jwtService;
    private final RedisDao redisDao;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
            String refreshToken) throws IOException{

        String email = jwtService.extractEmail(
                jwtService.extractClaims(refreshToken, "refresh"));

        String findRefreshToken = redisDao.getRefreshToken(email);

        log.info("redis {}",findRefreshToken);
        log.info("refreshToken {}", refreshToken);

        // redis에 refresh token이 존재하는지 여부와 일치여부를 확인하고 맞으면 access token과 refresh token 재발급
        if(findRefreshToken != null && findRefreshToken.equals(refreshToken)){

            User findUser = userRepository.findByEmail(email).orElseThrow(() ->
                    new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "존재하지 않는 유저 입니다."));

            String reIssuedRefreshToken = reIssueRefreshToken(email);

            jwtService.sendAccessAndRefreshToken(
                    response,
                    jwtService.generateAccessToken(
                            findUser.getId(),
                            findUser.getEmail(),
                            findUser.getUserType()),
                    reIssuedRefreshToken);

        }else {

            throw new ApiException(HttpStatus.UNAUTHORIZED, INVALID_PARAMETER, "유효하지 않은 리프레시 토큰입니다.");
        }
    }

    // refresh token 재발급 및 redis에 업데이트
    private String reIssueRefreshToken(String email) {

        String refreshToken = jwtService.generateRefreshToken(email).substring(7);

        redisDao.setRefreshToken(email, refreshToken, refreshExpiration);

        return refreshToken;
    }

    // 블랙리스트 확인 부분 추가해야함 -> 추가
    // 액세스 토큰 유효성 검사 및 authentication에 값 넣어줌
    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        jwtService.substringToken(request, "access")
                .filter(accessToken -> jwtService.isValidToken(accessToken, "access"))
                .ifPresent(accessToken -> {
                    Claims claims = jwtService.extractClaims(accessToken, "access");

                    String email = jwtService.extractEmail(claims);

                    String blackList = redisDao.getBlackList(accessToken);

                    log.info("access {} blackList {}", accessToken,blackList);

                    if(blackList != null){
                        throw new ApiException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN, "유효하지 않은 access 토큰입니다.");
                    }

                    User findUser = userRepository.findByEmail(email).orElseThrow(() ->
                            new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "존재하지 않는 유저 입니다."));

                    saveAuthentication(findUser);

                    request.setAttribute("userId", Long.parseLong((String) claims.get("userId")));
                    request.setAttribute("email", claims.get("email"));
                    request.setAttribute("userType", claims.get("userType"));
                    });

        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(User user){
        String password = user.getPassword();

//        if(password == null){
//            password = passwordEncoder.encode(RandomUtil.generatRandowPassword());
//        }

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_"+user.getUserType().name()));

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                authorities)
                ;

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


    }

}
