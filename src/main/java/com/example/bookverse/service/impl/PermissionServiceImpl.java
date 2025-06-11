package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.exception.role.ExistPermissionNameException;
import com.example.bookverse.repository.PermissionRepository;
import com.example.bookverse.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    final private PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission create(Permission permission) throws Exception {
        if (this.permissionRepository.existsByName(permission.getName())) {
            throw new ExistPermissionNameException( permission.getName() + "already exists");
        }
        return this.permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission) throws Exception {
        if (!this.permissionRepository.existsById(permission.getId())) {
            throw new IdInvalidException("Id invalid");
        }
        Permission permissionInDB = this.permissionRepository.findById(permission.getId()).orElse(null);
        if (permissionInDB != null) {
            if (permission.getName() != null && !permission.getName().equals(permissionInDB.getName())) {
                permissionInDB.setName(permission.getName());
            }
            if (permission.getApiPath() != null && !permission.getApiPath().equals(permissionInDB.getApiPath())) {
                permissionInDB.setApiPath(permission.getApiPath());
            }
            if (permission.getMethod() != null && !permission.getMethod().equals(permissionInDB.getMethod())) {
                permissionInDB.setMethod(permission.getMethod());
            }
            return this.permissionRepository.save(permissionInDB);
        }
        return null;
    }

    @Override
    public Permission fetchPermissionById(long id) throws Exception {
        if (!this.permissionRepository.existsById(id)) {
            throw new IdInvalidException("Id invalid");
        }
        return this.permissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Permission> fetchAllPermissions() throws Exception {
        return this.permissionRepository.findAll();
    }

    @Override
    public void delete(long id) throws Exception {
        if (!this.permissionRepository.existsById(id)) {
            throw new IdInvalidException("Id invalid");
        }
        this.permissionRepository.deleteById(id);
    }
}
