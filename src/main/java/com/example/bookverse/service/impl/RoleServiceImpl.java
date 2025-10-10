package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.QRole;
import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.criteria.CriteriaFilterRole;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.PermissionRepository;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.service.RoleService;
import com.example.bookverse.util.EntityValidator;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final EntityManager entityManager;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Role create(Role role) throws Exception {
        if (this.roleRepository.existsByName(role.getName())) {
            throw new ExistDataException(role.getName() + " already exist");
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
        Role roleInDB = FindObjectInDataBase.findByIdOrThrow(roleRepository, role.getId());
        if (role.getName() != null && !role.getName().equals(roleInDB.getName())) {
            roleInDB.setName(role.getName());
        }
        if (role.getDescription() != null && !role.getDescription().equals(roleInDB.getDescription())) {
            roleInDB.setDescription(role.getDescription());
        }
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

    @Override
    public Role fetchRoleById(long id) throws IdInvalidException {
        return FindObjectInDataBase.findByIdOrThrow(roleRepository, id);
    }

    @Override
    public List<Role> fetchAllRole() {
        return this.roleRepository.findAll();
    }

    public Page<Role> filter(CriteriaFilterRole criteriaFilterRole, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRole qRole = QRole.role;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterRole.getName() != null && !criteriaFilterRole.getName().isBlank()) {
            builder.and(qRole.name.containsIgnoreCase(criteriaFilterRole.getName()));
        }

        if (criteriaFilterRole.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterRole.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qRole.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Role> roles = queryFactory.selectFrom(qRole)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qRole)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(roles, pageable, total);
    }

    @Override
    public ResPagination fetchAllRoleWithPaginationAndFilter(CriteriaFilterRole criteriaFilterRole, Pageable pageable) throws Exception {
        Page<Role> rolePage = this.filter(criteriaFilterRole, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(rolePage.getSize());

        mt.setPages(rolePage.getTotalPages());
        mt.setTotal(rolePage.getTotalElements());

        rs.setMeta(mt);

        List<Role> roles = rolePage.getContent();

        rs.setResult(roles);

        return rs;
    }

    @Override
    public void delete(long id) throws IdInvalidException {
        FindObjectInDataBase.findByIdOrThrow(roleRepository, id);
        this.roleRepository.deleteById(id);
    }
}
