package com.example.bookverse.domain;

import com.example.bookverse.dto.enums.OrderPaymentStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "order_payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "provider_ref", nullable = false, unique = true, length = 64)
    private String providerRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 32)
    private PaymentMethod method;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderPaymentStatus status;

    @Column(name = "gateway_transaction_id", length = 128)
    private String gatewayTransactionId;

    @Column(name = "gateway_response_code", length = 32)
    private String gatewayResponseCode;

    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;

    private Instant completedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        createdAt = Instant.now();
        createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        updatedAt = Instant.now();
        updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }
}
