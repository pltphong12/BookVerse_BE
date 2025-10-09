package com.example.bookverse.service;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.criteria.CriteriaFilterPermission;
import com.example.bookverse.domain.response.ResPagination;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PermissionService {
    // Create
    Permission create(Permission permission) throws Exception;

    // Update
    Permission update(Permission permission) throws Exception;

    // fetch
    Permission fetchPermissionById(long id) throws Exception;

    // Fetch all
    List<Permission> fetchAllPermissions() throws Exception;

    // Fetch all filter
    ResPagination fetchAllPermissionWithPaginationAndFilter(CriteriaFilterPermission criteriaFilterPermission, Pageable pageable) throws Exception;

    // Delete
    void delete(long id) throws Exception;
}
