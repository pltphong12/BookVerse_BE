package com.example.bookverse.controller;

import com.example.bookverse.domain.Role;
import com.example.bookverse.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws Exception {
        Role newRole = this.roleService.create(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    // Update a role
    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws Exception {
        Role updatedRole = this.roleService.update(role);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    // Get a role by id
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) throws Exception {
        Role role = this.roleService.fetchRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    // Get all roles
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() throws Exception {
        List<Role> roles = this.roleService.fetchAllRole();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    // Delete a role
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws Exception {
        this.roleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
