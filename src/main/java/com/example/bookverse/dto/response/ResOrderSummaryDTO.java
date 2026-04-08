package com.example.bookverse.dto.response;

import com.example.bookverse.domain.Order;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Bản rút gọn đơn hàng cho danh sách có phân trang (không tải chi tiết dòng sách).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResOrderSummaryDTO {

    private long id;
    private String orderCode;
    private double totalPrice;
    private double subtotal;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Long customerId;
    private String receiverName;
    private String receiverPhone;
    private Instant createdAt;

    public static ResOrderSummaryDTO from(Order order) {
        ResOrderSummaryDTO dto = new ResOrderSummaryDTO();
        dto.setId(order.getId());
        dto.setOrderCode(order.getOrderCode());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setSubtotal(order.getSubtotal());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
        }
        dto.setReceiverName(order.getReceiverName());
        dto.setReceiverPhone(order.getReceiverPhone());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
