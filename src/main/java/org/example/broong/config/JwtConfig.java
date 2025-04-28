package org.example.broong.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.broong.security.auth.CustomUserDetailsService;
import org.example.broong.security.auth.CustomUsernamePasswordAuthenticationFilter;
import org.example.broong.security.jwt.JwtFilter;
import org.example.broong.security.jwt.JwtService;
import org.example.broong.security.auth.LoginFailureHandler;
import org.example.broong.security.auth.LoginSuccessHandler;
import org.example.broong.security.auth.RedisDao;
import org.example.broong.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RedisDao redisDao, AuthenticationManager authenticationManager) throws Exception {
        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler(redisDao));
        filter.setAuthenticationFailureHandler(loginFailureHandler());

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signup", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter(redisDao), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public JwtFilter jwtFilter(RedisDao redisDao) {
        return new JwtFilter(jwtService, redisDao, userRepository);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(RedisDao redisDao) {
        return new LoginSuccessHandler(jwtService, redisDao);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }
}

