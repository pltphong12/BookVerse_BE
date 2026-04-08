package com.example.bookverse.dto.criteria;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaFilterOrder {

    /** Lọc theo mã đơn (contains, không phân biệt hoa thường). */
    private String orderCode;

    /** Lọc theo khách hàng. */
    private Long customerId;

    /** Giá trị enum {@link com.example.bookverse.dto.enums.OrderStatus}, ví dụ {@code PENDING}. */
    private String status;

    /** {@link com.example.bookverse.dto.enums.PaymentMethod}: {@code COD}, {@code VNPAY}. */
    private String paymentMethod;

    /** {@link com.example.bookverse.dto.enums.PaymentStatus}. */
    private String paymentStatus;

    private LocalDate dateFrom;
    private LocalDate dateTo;
}
