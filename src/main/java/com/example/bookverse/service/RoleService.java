package com.example.bookverse.service;

import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.criteria.CriteriaFilterRole;
import com.example.bookverse.domain.response.ResPagination;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

    // Fetch all filter
    ResPagination fetchAllRoleWithPaginationAndFilter(CriteriaFilterRole criteriaFilterRole, Pageable pageable) throws Exception;

    // Delete a role
    void delete(long id) throws Exception;
}
