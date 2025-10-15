package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Supplier;
import com.example.bookverse.domain.criteria.CriteriaFilterSupplier;
import com.example.bookverse.domain.response.ResPagination;

public interface SupplierService {
    // Create
    Supplier create(Supplier supplier) throws Exception;
    // Update
    Supplier update(Supplier supplier) throws Exception;
    // Fetch a one
    Supplier fetchSupplierById(long id) throws Exception;
    // Fetch all
    List<Supplier> fetchAllSuppliers() throws Exception;
    // Fetch all with pagination and filter
    ResPagination fetchAllSuppliersWithPaginationAndFilter(CriteriaFilterSupplier criteriaFilterSupplier, Pageable pageable) throws Exception;
    // Delete
    void delete(long id) throws Exception;
}