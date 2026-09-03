package com.example.bookverse.repository;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findAllByCustomerOrderByIsDefaultDescCreatedAtDesc(Customer customer);

    Optional<CustomerAddress> findByIdAndCustomer(long id, Customer customer);
}
