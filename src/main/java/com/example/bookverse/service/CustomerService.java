package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.criteria.CriteriaFilterCustomer;
import com.example.bookverse.domain.response.ResPagination;

public interface CustomerService {
    // Create an customer
    Customer create(Customer customer) throws Exception;

    // Update an customer
    Customer update(Customer customer) throws Exception;

    // Fetch an customer by id
    Customer fetchCustomerById(long id) throws Exception;

    // Fetch all customers
    List<Customer> fetchAllCustomers() throws Exception;

    // // Fetch all customers with pagination and filter
    ResPagination fetchAllCustomersWithPaginationAndFilter(CriteriaFilterCustomer criteriaFilterCustomer, Pageable pageable) throws Exception;

    // Delete an customer
    void delete(long id) throws Exception;
}
