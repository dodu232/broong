package org.example.broong.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.global.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${access.secret.key}")
    private String accessSecretKey;
    @Value("${refresh.secret.key}")
    private String refreshSecretKey;
    @Value("${access.expiration}")
    private long accessExpiration;
    @Value("${refresh.expiration}")
    private long refreshExpiration;
    @Value("${access.header}")
    private String accessHeader;
    @Value("${refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    private Key keyAc;
    private Key keyRf;

    @PostConstruct
    public void init() {
        byte[] bytes_ac = Base64.getDecoder().decode(accessSecretKey);
        keyAc = Keys.hmacShaKeyFor(bytes_ac);
        byte[] bytes_rf = Base64.getDecoder().decode(refreshSecretKey);
        keyRf = Keys.hmacShaKeyFor(bytes_rf);
    }

    private static final String BEARER_PREFIX = "Bearer ";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // Generator

    // access token 생성
    public String generateAccessToken(Long userId, String email, UserType usertype) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(ACCESS_TOKEN_SUBJECT)
                        .claim("userId", String.valueOf(userId))
                        .claim("email", email)
                        .claim("userType", usertype)
                        .setExpiration(new Date(date.getTime() + accessExpiration))
                        .setIssuedAt(date)
                        .signWith(keyAc, signatureAlgorithm)
                        .compact();
    }

    // refresh token 생성
    public String generateRefreshToken(String email) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(REFRESH_TOKEN_SUBJECT)
                        .claim("email", email)
                        .setExpiration(new Date(date.getTime() + refreshExpiration))
                        .setIssuedAt(date)
                        .signWith(keyRf, signatureAlgorithm)
                        .compact();
    }

    // Parser

    // token 앞에 글자 삭제
    public Optional<String> substringToken(HttpServletRequest request, String type) {
        String header = resolveHeader(type);

        if (header == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(request.getHeader(header))
                .filter(refreshToken -> refreshToken.startsWith("Bearer "))
                .map(refreshToken -> refreshToken.substring(7));

    }


    // token 값 추출
    public Claims extractClaims(String token, String type) {

        Key key = resolveKey(type);

        if (key == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "Key값이 null 값입니다.");
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (claims == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, NO_RESOURCE, "잘못된 JWT 토큰입니다.");
        }

        return claims;
    }

    // email 값 가져오기
    public String extractEmail(Claims claims) {
        return claims.get("email").toString();
    }


    // 헤더에 토큰 값 설정
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);

        log.info("Access Token, Refresh Token 설정 완료");

    }

    // validator

    // 유효성 검사
    public boolean isValidToken(String token, String type) {
        try {

            Key key = resolveKey(type);

            if (key == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_PARAMETER, "Key값이 null 값입니다.");
            }

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
        }
        return false;
    }

    // 헤더 타입 결정
    private String resolveHeader(String type) {
        if (type.equals("access")) {
            return accessHeader;
        }
        if (type.equals("refresh")) {
            return refreshHeader;
        }
        return null;
    }

    // 시크릿 키 결정
    private Key resolveKey(String type) {
        if (type.equals("access")) {
            return keyAc;
        }
        if (type.equals("refresh")) {
            return keyRf;
        }
        return null;

    }


}
