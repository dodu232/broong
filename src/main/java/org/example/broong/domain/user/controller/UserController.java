package org.example.broong.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.user.dto.DeleteUserRequestDto;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestBody
            DeleteUserRequestDto requestDto, HttpServletRequest httpServletRequest){

        Long userId = customUserDetails.getUserId();
        userService.deleteUser(httpServletRequest, userId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴 되었습니다.");
    }



}
