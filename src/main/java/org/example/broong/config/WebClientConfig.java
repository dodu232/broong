package org.example.broong.config;

import lombok.RequiredArgsConstructor;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    private final PaymentProperties props;

    @Bean
    public WebClient tossWebClient(){
        String creds = props.getSecretKey() + ":";
        String basicAuth = "Basic " + Base64.getEncoder()
                .encodeToString(creds.getBytes(StandardCharsets.UTF_8));

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuth)
                .filter(logRequest())
                .filter(handleErrors())
                .build();
    }

    private ExchangeFilterFunction logRequest(){
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            log.info("[toss-req] {} {}", req.method(), req.url());
            req.headers().forEach((name, vals) -> log.debug(name + ": " + vals));
            return Mono.just(req);
        });
    }

    private ExchangeFilterFunction handleErrors() {
        return ExchangeFilterFunction.ofResponseProcessor(resp -> {
            if(resp.statusCode().isError()) {
                HttpStatus status = (HttpStatus) resp.statusCode();
                return resp.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new ApiException(status, ErrorType.EXTERNAL_API_ERROR, body)));
            }
            return Mono.just(resp);
        });
    }

}
