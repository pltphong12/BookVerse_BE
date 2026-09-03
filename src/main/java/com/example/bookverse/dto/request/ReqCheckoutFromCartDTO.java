package com.example.bookverse.dto.request;

import com.example.bookverse.dto.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCheckoutFromCartDTO {
    @NotNull(message = "shippingAddressId không được để trống")
    private Long shippingAddressId;

    @NotNull(message = "paymentMethod không được để trống")
    private PaymentMethod paymentMethod;

    private String note;

    @JsonIgnore
    private String clientIpAddress;
}
