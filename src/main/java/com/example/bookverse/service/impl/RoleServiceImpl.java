package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Role;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.exception.role.ExistRoleNameException;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) throws ExistRoleNameException {
        if (this.roleRepository.existsByName(role.getName())) {
            throw new ExistRoleNameException(role.getName() + " already exist");
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
