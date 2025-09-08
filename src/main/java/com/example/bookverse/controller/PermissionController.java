package com.example.bookverse.controller;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.PermissionService;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws Exception {
        Permission newPermission = this.permissionService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPermission);
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws Exception {
        Permission updatedPermission = this.permissionService.update(permission);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPermission);
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable long id) throws Exception {
        Permission permission = this.permissionService.fetchPermissionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(permission);
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getAllPermissions() throws Exception {
        List<Permission> permissions = this.permissionService.fetchAllPermissions();
        return ResponseEntity.status(HttpStatus.OK).body(permissions);
    }

    @GetMapping("/permissions/search")
    public ResponseEntity<ResPagination> getAllWithPaginationAndFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String method,
            @RequestParam(name = "date_from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            Pageable pageable)throws Exception {
        ResPagination resPagination = this.permissionService.fetchAllPermissionWithPaginationAndFilter(name, method, dateFrom, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable long id) throws Exception {
        this.permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
