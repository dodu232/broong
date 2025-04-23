package org.example.broong.domain.auto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.auto.dto.request.SignupRequestDto;
import org.example.broong.domain.auto.dto.response.SignupResponseDto;
import org.example.broong.domain.auto.service.AutoService;
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
    public SignupResponseDto signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return autoService.signup(requestDto);
    }


}
