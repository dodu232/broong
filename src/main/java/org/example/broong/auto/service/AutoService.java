package org.example.broong.auto.service;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;
import lombok.RequiredArgsConstructor;
import org.example.broong.auto.dto.request.SignupRequestDto;
import org.example.broong.auto.dto.response.SignupResponseDto;
import org.example.broong.config.JwtUtil;
import org.example.broong.config.PasswordEncoder;
import org.example.broong.global.exception.ApiException;
import org.example.broong.user.entity.User;
import org.example.broong.user.enums.UserType;
import org.example.broong.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponseDto signup(SignupRequestDto requestDto) {

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new ApiException(HttpStatus.CONFLICT, INVALID_PARAMETER, "이미 존재하는 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserType userType = UserType.of(requestDto.getUserType());

        User newUser = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                encodedPassword,
                userType
        );

        newUser.addPoint(100);

        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(),
                savedUser.getUserType());

        return new SignupResponseDto(bearerToken);

    }
}
