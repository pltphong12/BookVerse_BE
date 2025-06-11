package com.example.bookverse.service;

import com.example.bookverse.domain.Permission;

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

    // Delete
    void delete(long id) throws Exception;
}
