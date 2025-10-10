package com.example.bookverse.controller;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.criteria.CriteriaFilterCustomer;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.customer.ResCustomerDTO;
import com.example.bookverse.service.CustomerService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    public ResponseEntity<ResCustomerDTO> createCustomer(@RequestBody Customer customer) throws Exception {
        Customer newCustomer = this.customerService.create(customer);
        ResCustomerDTO resCustomerDTO = modelMapper.map(newCustomer, ResCustomerDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCustomerDTO);
    }

    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    public ResponseEntity<ResCustomerDTO> updateCustomer(@RequestBody Customer customer) throws Exception {
        Customer updatedCustomer = this.customerService.update(customer);
        ResCustomerDTO resCustomerDTO = modelMapper.map(updatedCustomer, ResCustomerDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(resCustomerDTO);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW_BY_ID')")
    public ResponseEntity<ResCustomerDTO> getCustomer(@PathVariable long id) throws Exception {
        Customer customer = this.customerService.fetchCustomerById(id);
        ResCustomerDTO resCustomerDTO = modelMapper.map(customer, ResCustomerDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(resCustomerDTO);
    }

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW_ALL')")
    public ResponseEntity<ResPagination> getCustomersWithPagination(
            @ModelAttribute CriteriaFilterCustomer criteriaFilterCustomer,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.customerService.fetchAllCustomersWithPaginationAndFilter(criteriaFilterCustomer, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }
}
