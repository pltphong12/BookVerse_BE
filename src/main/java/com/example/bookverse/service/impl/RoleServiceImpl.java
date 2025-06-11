package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.Role;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.exception.role.ExistRoleNameException;
import com.example.bookverse.repository.PermissionRepository;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.service.RoleService;
import com.example.bookverse.util.EntityValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Role create(Role role) throws ExistRoleNameException, IdInvalidException {
        if (this.roleRepository.existsByName(role.getName())) {
            throw new ExistRoleNameException(role.getName() + " already exist");
        }
        if (role.getPermissions() != null) {
            List<Long> permissionIds = role.getPermissions().stream()
                    .map(Permission::getId) // Get ID of aLl permissions
                    .toList(); // Convert List<Long>
            EntityValidator.validateIdsExist(permissionIds,permissionRepository,"Permission");
            role.setPermissions(role.getPermissions());
        }
        return this.roleRepository.save(role);
    }

    @Override
    public Role update(Role role) throws IdInvalidException {
        Role roleInDB = this.roleRepository.findById(role.getId()).orElse(null);
        if (roleInDB == null) {
            throw new IdInvalidException("Role not found");
        }
        else {
            if (role.getDescription() != null && !role.getDescription().equals(roleInDB.getDescription())) {
                roleInDB.setDescription(role.getDescription());
            }
            if (role.getPermissions() != null && !role.getPermissions().equals(roleInDB.getPermissions())) {
                List<Long> permissionIds = role.getPermissions().stream()
                        .map(Permission::getId) // Get ID of all permission
                        .toList(); // Convert List<Long>
                EntityValidator.validateIdsExist(permissionIds,permissionRepository,"Permission");
                roleInDB.setPermissions(role.getPermissions());
            }
            return roleRepository.save(roleInDB);
        }
    }

    @Override
    public Role fetchRoleById(long id) throws IdInvalidException {
        if (!this.roleRepository.existsById(id)) {
            throw new IdInvalidException("Role not found");
        }
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> fetchAllRole() {
        return this.roleRepository.findAll();
    }

    @Override
    public void delete(long id) throws IdInvalidException {
        if (!this.roleRepository.existsById(id)) {
            throw new IdInvalidException("Role not found");
        }
        this.roleRepository.deleteById(id);
    }
}
