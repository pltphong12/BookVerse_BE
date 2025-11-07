package com.example.bookverse.service;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.criteria.CriteriaFilterCustomer;
import com.example.bookverse.domain.request.ReqCustomerDTO;
import com.example.bookverse.domain.response.ResCustomerDTO;
import com.example.bookverse.domain.response.ResPagination;

public interface CustomerService {
    // Create a customer
    ResCustomerDTO create(ReqCustomerDTO reqCustomerDTO) throws Exception;

    // Update a customer
    ResCustomerDTO update(ReqCustomerDTO reqCustomerDTO) throws Exception;

    // Fetch a customer by id
    ResCustomerDTO fetchCustomerById(long id) throws Exception;

    // Fetch all customers with pagination and filter
    ResPagination fetchAllCustomersWithPaginationAndFilter(CriteriaFilterCustomer criteriaFilterCustomer, Pageable pageable) throws Exception;
    
    // Delete a customer by id
    void delete(long id) throws Exception;

}