package com.example.bookverse.dto.request;

import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqUpdateOrderDTO {

    @NotNull(message = "id đơn hàng không được để trống")
    private Long id;

    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverEmail;

    /** Chỉ tài khoản có {@code ORDER_VIEW_ALL} mới được đổi. */
    private OrderStatus status;

    /**
     * Trạng thái thanh toán (COD đã thu, hoàn tiền…). Chỉ role có {@code ORDER_VIEW_ALL}.
     * Khi đặt {@link PaymentStatus#PAID} và đơn chưa có {@code paidAt}, server ghi nhận thời điểm hiện tại.
     */
    private PaymentStatus paymentStatus;
}
