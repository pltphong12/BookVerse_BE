package com.example.bookverse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookverse.domain.Customer;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {
    boolean existsByIdentityCard(String identityCard);
}
