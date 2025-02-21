package com.bang.WebBanHang_Project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment.gateway")
@Data
public class PaymentGatewayConfig {
    private String baseUrl;
    private String merchantId;
    private String apiKey;
    private String secretKey;
    private String returnUrl;
    private String notifyUrl;
}
