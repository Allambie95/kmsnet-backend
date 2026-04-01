package com.example.backend.BackEnd.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class WebpayConfig {

    @Value("${webpay.commerce-code}")
    private String commerceCode;

    @Value("${webpay.api-key}")
    private String apiKey;

    @Value("${webpay.return-url}")
    private String returnUrl;
}
