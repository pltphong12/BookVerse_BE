package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.criteria.CriteriaFilterPermission;
import com.example.bookverse.domain.response.ResPagination;

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
