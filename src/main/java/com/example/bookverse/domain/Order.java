package com.example.bookverse.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import com.example.bookverse.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_code", unique = true, length = 40)
    private String orderCode;

    private double totalPrice;
    private double subtotal;
    private double shippingFee;
    private double discountTotal;

    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverEmail;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private PaymentStatus paymentStatus;

    private Instant paidAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<OrderPayment> orderPayments;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate(){
        createdAt = Instant.now();
        createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (orderCode == null || orderCode.isBlank()) {
            orderCode = "BV-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        updatedAt = Instant.now();
        updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
