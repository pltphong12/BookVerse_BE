package com.example.bookverse.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bookverse.shipping")
@Getter
@Setter
public class ShippingProperties {
    private double standardFee = 30000;
    private double freeShippingThreshold = 500000;
}
