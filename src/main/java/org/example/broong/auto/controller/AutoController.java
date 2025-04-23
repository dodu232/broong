package org.example.broong.auto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.auto.dto.request.SignupRequestDto;
import org.example.broong.auto.dto.response.SignupResponseDto;
import org.example.broong.auto.service.AutoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutoController {

    private final AutoService autoService;

    @PostMapping("{auto/signup}")
    public SignupResponseDto signup(@Valid @RequestBody SignupRequestDto requestDto){
        return autoService.signup(requestDto);
    }



}
