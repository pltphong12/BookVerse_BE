package com.example.bookverse.repository;

import com.example.bookverse.domain.OrderPayment;
import com.example.bookverse.domain.Order;
import com.example.bookverse.dto.enums.OrderPaymentStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {

    Optional<OrderPayment> findByProviderRef(String providerRef);

    Optional<OrderPayment> findByOrder(Order order);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM OrderPayment p JOIN FETCH p.order WHERE p.providerRef = :providerRef")
    Optional<OrderPayment> findByProviderRefForUpdate(String providerRef);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p FROM OrderPayment p
            JOIN FETCH p.order
            WHERE p.method = :method
            AND p.status = :status
            AND p.createdAt < :cutoff
            """)
    List<OrderPayment> findExpiredPaymentsForUpdate(PaymentMethod method, OrderPaymentStatus status, Instant cutoff);
}
