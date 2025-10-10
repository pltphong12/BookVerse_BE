package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.QPermission;
import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.criteria.CriteriaFilterPermission;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.PermissionRepository;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.service.PermissionService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class PermissionServiceImpl implements PermissionService {
    final private PermissionRepository permissionRepository;
    final private RoleRepository roleRepository;
    final private EntityManager entityManager;

    public PermissionServiceImpl(PermissionRepository permissionRepository, RoleRepository roleRepository, EntityManager entityManager) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Permission create(Permission permission) throws Exception {
        if (this.permissionRepository.existsByName(permission.getName())) {
            throw new ExistDataException(permission.getName() + "already exists");
        }
        return this.permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission) throws Exception {
        Permission permissionInDB = FindObjectInDataBase.findByIdOrThrow(permissionRepository, permission.getId());
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

    @Override
    public Permission fetchPermissionById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(permissionRepository, id);
    }

    @Override
    public List<Permission> fetchAllPermissions() throws Exception {
        return this.permissionRepository.findAll();
    }

    public Page<Permission> filter(CriteriaFilterPermission criteriaFilterPermission, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPermission qPermission = QPermission.permission;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterPermission.getName() != null && !criteriaFilterPermission.getName().isBlank()) {
            builder.and(qPermission.name.containsIgnoreCase(criteriaFilterPermission.getName()));
        }
        if (criteriaFilterPermission.getMethod() != null && !criteriaFilterPermission.getMethod().isBlank()) {
            builder.and(qPermission.method.eq(criteriaFilterPermission.getMethod()));
        }
        if (criteriaFilterPermission.getDomain() != null && !criteriaFilterPermission.getDomain().isBlank()) {
            builder.and(qPermission.domain.containsIgnoreCase(criteriaFilterPermission.getDomain()));
        }
        if (criteriaFilterPermission.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterPermission.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qPermission.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Permission> permissions = queryFactory.selectFrom(qPermission)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qPermission)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(permissions, pageable, total);
    }

    @Override
    public ResPagination fetchAllPermissionWithPaginationAndFilter(CriteriaFilterPermission criteriaFilterPermission, Pageable pageable) throws Exception {
        Page<Permission> permissionPage = this.filter(criteriaFilterPermission, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(permissionPage.getSize());

        mt.setPages(permissionPage.getTotalPages());
        mt.setTotal(permissionPage.getTotalElements());

        rs.setMeta(mt);

        List<Permission> permissions = permissionPage.getContent();

        rs.setResult(permissions);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        Permission permission = FindObjectInDataBase.findByIdOrThrow(permissionRepository, id);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        List<Role> roles = this.roleRepository.findAllByPermissions(permissions);
        for (Role role : roles) {
            role.getPermissions().remove(permissions.getFirst());
        }
        this.permissionRepository.deleteById(id);
    }
}
