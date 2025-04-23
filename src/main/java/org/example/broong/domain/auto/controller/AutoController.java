package org.example.broong.domain.auto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.auto.dto.request.AutoRequestDto;
import org.example.broong.domain.auto.dto.response.AutoResponseDto;
import org.example.broong.domain.auto.service.AutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auto")
public class AutoController {

    private final AutoService autoService;

    @PostMapping("/signup")
    public ResponseEntity<AutoResponseDto.Signup> signup(@Valid @RequestBody AutoRequestDto.Singup requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autoService.signup(requestDto));
    }

}
