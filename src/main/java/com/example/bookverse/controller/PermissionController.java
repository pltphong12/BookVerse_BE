package com.example.bookverse.controller;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.criteria.CriteriaFilterPermission;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.PermissionService;
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
public class PermissionController {
    final private PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws Exception {
        Permission newPermission = this.permissionService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPermission);
    }

    @PutMapping("/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws Exception {
        Permission updatedPermission = this.permissionService.update(permission);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPermission);
    }

    @GetMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_BY_ID')")
    public ResponseEntity<Permission> getPermissionById(@PathVariable long id) throws Exception {
        Permission permission = this.permissionService.fetchPermissionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(permission);
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_ALL')")
    public ResponseEntity<List<Permission>> getAllPermissions() throws Exception {
        List<Permission> permissions = this.permissionService.fetchAllPermissions();
        return ResponseEntity.status(HttpStatus.OK).body(permissions);
    }

    @GetMapping("/permissions/search")
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getAllWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterPermission criteriaFilterPermission,
            Pageable pageable)throws Exception {
        ResPagination resPagination = this.permissionService.fetchAllPermissionWithPaginationAndFilter(criteriaFilterPermission, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    @DeleteMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<Void> deletePermission(@PathVariable long id) throws Exception {
        this.permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
