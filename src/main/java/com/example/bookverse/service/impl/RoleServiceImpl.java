package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Role;
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
    public Role create(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role fetchRoleById(long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> fetchAllRole() {
        return this.roleRepository.findAll();
    }

    @Override
    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }
}
