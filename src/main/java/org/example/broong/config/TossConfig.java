package org.example.broong.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Configuration
public class TossConfig {
    @Value("{toss.secretKey}") String secretKey;
    @Value("{toss.baseUrl}") String baseUrl;

    @Bean
    public WebClient tossWebClient() {
        String basicAuth = Base64.getEncoder()
                .encodeToString((secretKey+ ":").getBytes());
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic" + basicAuth)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

}
