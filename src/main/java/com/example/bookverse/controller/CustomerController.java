package com.example.bookverse.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookverse.domain.criteria.CriteriaFilterCustomer;
import com.example.bookverse.domain.request.ReqCustomerDTO;
import com.example.bookverse.domain.response.ResCustomerDTO;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.CustomerService;

@RequestMapping("/api/v1")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    public ResponseEntity<ResCustomerDTO> createCustomer(@RequestBody ReqCustomerDTO reqCustomerDTO) throws Exception {
        ResCustomerDTO newCustomer = this.customerService.create(reqCustomerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
    }

    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    public ResponseEntity<ResCustomerDTO> updateCustomer(@RequestBody ReqCustomerDTO reqCustomerDTO) throws Exception {
        ResCustomerDTO updatedCustomer = this.customerService.update(reqCustomerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW_BY_ID')")
    public ResponseEntity<ResCustomerDTO> getCustomerById(@PathVariable Long id) throws Exception {
        ResCustomerDTO customer = this.customerService.fetchCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) throws Exception {
        this.customerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/customers/search")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getCustomersWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterCustomer criteriaFilterCustomer,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.customerService
                .fetchAllCustomersWithPaginationAndFilter(criteriaFilterCustomer, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

}