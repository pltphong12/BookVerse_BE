package com.example.bookverse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCustomerAddressDTO {
    private Long id;

    @NotBlank(message = "receiverName không được để trống")
    private String receiverName;

    @NotBlank(message = "receiverPhone không được để trống")
    private String receiverPhone;

    @NotBlank(message = "province không được để trống")
    private String province;

    @NotBlank(message = "district không được để trống")
    private String district;

    @NotBlank(message = "ward không được để trống")
    private String ward;

    @NotBlank(message = "addressLine không được để trống")
    private String addressLine;

    @NotNull(message = "isDefault không được để trống")
    private Boolean isDefault;
}
