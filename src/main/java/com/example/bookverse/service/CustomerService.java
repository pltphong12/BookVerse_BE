package com.example.bookverse.service;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.dto.criteria.CriteriaFilterCustomer;
import com.example.bookverse.dto.request.ReqCustomerDTO;
import com.example.bookverse.dto.response.ResCustomerDTO;
import com.example.bookverse.dto.response.ResPagination;

public interface CustomerService {
    ResCustomerDTO create(ReqCustomerDTO reqCustomerDTO) throws Exception;

    ResCustomerDTO update(ReqCustomerDTO reqCustomerDTO) throws Exception;

    ResCustomerDTO fetchCustomerById(long id) throws Exception;

    ResPagination fetchAllCustomersWithPaginationAndFilter(CriteriaFilterCustomer criteriaFilterCustomer, Pageable pageable) throws Exception;

    void delete(long id) throws Exception;
}