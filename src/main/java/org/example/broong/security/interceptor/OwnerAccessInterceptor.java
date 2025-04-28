package org.example.broong.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.broong.domain.user.enums.UserType;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class OwnerAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        log.info("UserType = {}", request.getAttribute("userType"));
        UserType userType = UserType.of((String) request.getAttribute("userType"));
        if(!UserType.OWNER.equals(userType)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Owner 권한이 필요합니다.");
            return false;
        }
        return true;
    }

}
