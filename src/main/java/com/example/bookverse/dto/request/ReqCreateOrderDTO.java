package com.example.bookverse.dto.request;

import com.example.bookverse.dto.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateOrderDTO {

    @NotBlank(message = "receiverName không được để trống")
    private String receiverName;

    @NotBlank(message = "receiverAddress không được để trống")
    private String receiverAddress;

    @NotBlank(message = "receiverPhone không được để trống")
    private String receiverPhone;

    private String receiverEmail;

    @NotNull(message = "paymentMethod không được để trống")
    private PaymentMethod paymentMethod;

    private String note;

    @NotEmpty(message = "Danh sách sản phẩm không được rỗng")
    @Valid
    private List<ReqOrderLineDTO> items;

    /** Controller set IP từ HttpServletRequest — không nhận từ JSON body. */
    @JsonIgnore
    private String clientIpAddress;
}
