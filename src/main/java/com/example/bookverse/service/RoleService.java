package com.example.bookverse.service;

import com.example.bookverse.domain.Role;

import java.util.List;

public interface RoleService {
    // Create a role
    Role create(Role role) throws Exception;

    // Update a role
    Role update(Role role) throws Exception;

    // Fetch a role
    Role fetchRoleById(long id) throws Exception;

    // Fetch all role
    List<Role> fetchAllRole() throws Exception;

    // Delete a role
    void delete(long id) throws Exception;
}
