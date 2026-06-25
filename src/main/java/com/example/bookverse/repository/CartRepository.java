package com.example.bookverse.repository;

import com.example.bookverse.domain.Cart;
import com.example.bookverse.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer(Customer customer);
}
