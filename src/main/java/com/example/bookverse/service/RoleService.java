package com.example.bookverse.service;

import com.example.bookverse.domain.Role;

import java.util.List;

public interface RoleService {
    // Create a role
    Role create(Role role);

    // Update a role
    Role update(Role role);

    // Fetch a role
    Role fetchRoleById(long id);

    // Fetch all role
    List<Role> fetchAllRole();

    // Delete a role
    void delete(long id);
}
