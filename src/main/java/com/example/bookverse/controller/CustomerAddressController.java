package com.example.bookverse.controller;

import com.example.bookverse.dto.request.ReqCustomerAddressDTO;
import com.example.bookverse.dto.response.ResCustomerAddressDTO;
import com.example.bookverse.service.CustomerAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class CustomerAddressController {
    private final CustomerAddressService customerAddressService;

    public CustomerAddressController(CustomerAddressService customerAddressService) {
        this.customerAddressService = customerAddressService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADDRESS_CREATE')")
    public ResponseEntity<ResCustomerAddressDTO> create(@Valid @RequestBody ReqCustomerAddressDTO request)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerAddressService.create(request));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADDRESS_UPDATE')")
    public ResponseEntity<ResCustomerAddressDTO> update(@Valid @RequestBody ReqCustomerAddressDTO request)
            throws Exception {
        return ResponseEntity.ok(customerAddressService.update(request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ADDRESS_VIEW_MINE')")
    public ResponseEntity<List<ResCustomerAddressDTO>> listMine() throws Exception {
        return ResponseEntity.ok(customerAddressService.listMine());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADDRESS_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable long id) throws Exception {
        customerAddressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
