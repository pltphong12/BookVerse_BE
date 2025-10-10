package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.criteria.CriteriaFilterRole;
import com.example.bookverse.domain.response.ResPagination;

public interface RoleService {
    // Create a role
    Role create(Role role) throws Exception;

    // Update a role
    Role update(Role role) throws Exception;

    // Fetch a role
    Role fetchRoleById(long id) throws Exception;

    // Fetch all role
    List<Role> fetchAllRole() throws Exception;

    // Fetch all filter
    ResPagination fetchAllRoleWithPaginationAndFilter(CriteriaFilterRole criteriaFilterRole, Pageable pageable) throws Exception;

    // Delete a role
    void delete(long id) throws Exception;
}
