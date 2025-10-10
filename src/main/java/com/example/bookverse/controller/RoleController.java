package com.example.bookverse.controller;

import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.criteria.CriteriaFilterRole;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Create a role
    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws Exception {
        Role newRole = this.roleService.create(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    // Update a role
    @PutMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws Exception {
        Role updatedRole = this.roleService.update(role);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    // Get a role by id
    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BY_ID')")
    public ResponseEntity<Role> getRole(@PathVariable Long id) throws Exception {
        Role role = this.roleService.fetchRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    // Get all roles
    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ALL')")
    public ResponseEntity<List<Role>> getAllRoles() throws Exception {
        List<Role> roles = this.roleService.fetchAllRole();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    // Get roles with pagination and filter
    @GetMapping("/roles/search")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getAllWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterRole criteriaFilterRole,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.roleService.fetchAllRoleWithPaginationAndFilter(criteriaFilterRole,
                pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    // Delete a role
    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws Exception {
        this.roleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
