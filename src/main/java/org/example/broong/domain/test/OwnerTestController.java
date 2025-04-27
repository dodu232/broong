package org.example.broong.domain.test;


import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.user.enums.UserType;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerTestController {

    @PostMapping("/test")
    public ResponseEntity<AuthUser> test(@AuthenticationPrincipal CustomUserDetails userDetails){
        String email = userDetails.getUsername();
        Long userId = userDetails.getUserId();
        UserType userType = userDetails.getUserType();

        // 이건 그냥 유저 정보 잘 꺼내올 수 있나확인용
        // 위와 같이 내용 꺼내와서 사용하면 됨
        AuthUser authUser = new AuthUser(userId,email,userType);

        return ResponseEntity.ok(authUser);
    }

}
