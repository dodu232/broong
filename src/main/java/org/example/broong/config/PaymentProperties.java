package org.example.broong.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {
    private String secretKey;
    private String baseUrl;
    private String confirmEndpoint;


}
