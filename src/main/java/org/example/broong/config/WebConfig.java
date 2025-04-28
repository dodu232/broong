package org.example.broong.config;

import lombok.RequiredArgsConstructor;
import org.example.broong.security.interceptor.OwnerAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OwnerAccessInterceptor())
                .addPathPatterns("/owner/**");
    }

}
