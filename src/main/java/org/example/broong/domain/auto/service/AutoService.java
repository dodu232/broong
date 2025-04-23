package org.example.broong.domain.auto.service;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.auto.dto.request.AutoRequestDto;
import org.example.broong.domain.auto.dto.response.AutoResponseDto;
import org.example.broong.config.JwtUtil;
import org.example.broong.config.PasswordEncoder;
import org.example.broong.domain.user.entity.User.UserBuilder;
import org.example.broong.global.exception.ApiException;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AutoResponseDto.Signup signup(AutoRequestDto.Singup requestDto) {

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new ApiException(HttpStatus.CONFLICT, INVALID_PARAMETER, "이미 존재하는 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserType userType = UserType.of(requestDto.getUserType());

        User newUser = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .userType(userType)
                .build();

        newUser.addPoint(100);

        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(),
                savedUser.getUserType());

        return new AutoResponseDto.Signup(bearerToken);

    }
}
