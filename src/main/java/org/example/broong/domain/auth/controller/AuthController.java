package org.example.broong.domain.auth.controller;

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
    public ResponseEntity<AuthResponseDto> signup(@Valid @RequestBody AuthRequestDto.Singup requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(requestDto));

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> signin(@RequestBody AuthRequestDto.Signin requestDto){

        return ResponseEntity.status(HttpStatus.OK).body(authService.signin(requestDto));

    }

}
