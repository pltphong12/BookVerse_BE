package com.example.bookverse.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bookverse.vnpay")
@Getter
@Setter
public class VnpayProperties {
    private String tmnCode;
    private String hashSecret;
    private String paymentUrl;
    private String returnUrl;

    private String version = "2.1.0";
    private String command = "pay";
    private String currCode = "VND";
    private String locale = "vn";
    private String orderType = "other";

    /**
     * Frontend URLs — backend redirect browser tới đây sau khi verify callback.
     */
    private String frontendSuccessUrl = "http://localhost:5173/payment/success";
    private String frontendFailUrl = "http://localhost:5173/payment/failure";
}
