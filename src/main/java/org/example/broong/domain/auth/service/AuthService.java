package org.example.broong.domain.auth.service;

import static org.example.broong.global.exception.ErrorType.INVALID_PARAMETER;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.auth.dto.request.AuthRequestDto;
import org.example.broong.domain.auth.dto.response.AuthResponseDto;
import org.example.broong.config.JwtUtil;
import org.example.broong.config.PasswordEncoder;
import org.example.broong.global.exception.ApiException;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponseDto signup(AuthRequestDto.Singup requestDto) {

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

        return new AuthResponseDto(bearerToken);

    }

//    public AutoResponseDto signin(AutoRequestDto.Signin requestDto) {
//        User user = userRepository.findByEmail(requestDto.getEmail())
//                .orElseThrow(()-> new ApiException(HttpStatus.UNAUTHORIZED, NO_RESOURCE, "가입되지 않은 유저입니다."));
//
//        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
//            throw new ApiException(HttpStatus.BAD_REQUEST,INVALID_PARAMETER, " 비밀번호가 일치하지 않습니다.");
//        }
//
//
//    }
}
