package org.example.broong.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.auth.dto.request.AuthRequestDto;
import org.example.broong.domain.auth.dto.response.AuthResponseDto;
import org.example.broong.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody AuthRequestDto.Singup requestDto) {

        authService.signup(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
//        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(requestDto));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


//    @PostMapping("/signin")
//    public ResponseEntity<AuthResponseDto> signin(@Valid @RequestBody AuthRequestDto.Signin requestDto){
//
//        return ResponseEntity.status(HttpStatus.OK).body(authService.signin(requestDto));
//
//    }

}
