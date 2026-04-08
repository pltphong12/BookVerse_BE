package com.example.bookverse.repository;

import com.example.bookverse.domain.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {

    Optional<OrderPayment> findByProviderRef(String providerRef);
}
