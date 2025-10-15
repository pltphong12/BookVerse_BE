package com.example.bookverse.controller;

import java.util.List;

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

import com.example.bookverse.domain.Supplier;
import com.example.bookverse.domain.criteria.CriteriaFilterSupplier;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/suppliers")
    @PreAuthorize("hasAuthority('SUPPLIER_CREATE')")
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) throws Exception {
        Supplier newSupplier = this.supplierService.create(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSupplier);
    }

    @PutMapping("/suppliers")
    @PreAuthorize("hasAuthority('SUPPLIER_UPDATE')")
    public ResponseEntity<Supplier> updateSupplier(@RequestBody Supplier supplier) throws Exception {
        Supplier updatedSupplier = this.supplierService.update(supplier);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSupplier);
    }

    @GetMapping("/suppliers/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW_BY_ID')")
    public ResponseEntity<Supplier> getSupplier(@PathVariable long id) throws Exception {
        Supplier supplier = this.supplierService.fetchSupplierById(id);
        return ResponseEntity.status(HttpStatus.OK).body(supplier);
    }
    
    @GetMapping("/suppliers")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW_ALL')")
    public ResponseEntity<List<Supplier>> getAllSuppliers() throws Exception {
        List<Supplier> suppliers = this.supplierService.fetchAllSuppliers();
        return ResponseEntity.status(HttpStatus.OK).body(suppliers);
    }

    @GetMapping("/suppliers/search")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getSuppliersWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterSupplier criteriaFilterSupplier,
            Pageable pageable) throws Exception {
        ResPagination suppliers = this.supplierService.fetchAllSuppliersWithPaginationAndFilter(criteriaFilterSupplier, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(suppliers);
    }

    @DeleteMapping("/suppliers/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_DELETE')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable long id) throws Exception {
        this.supplierService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
