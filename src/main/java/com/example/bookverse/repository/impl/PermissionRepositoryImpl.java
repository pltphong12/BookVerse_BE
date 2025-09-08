package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.QPermission;
import com.example.bookverse.repository.custom.PermissionRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Repository
public class PermissionRepositoryImpl implements PermissionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Permission> filter(String name, String method, LocalDate dateFrom, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPermission qPermission = QPermission.permission;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (name != null && !name.isBlank()) {
            builder.and(qPermission.name.containsIgnoreCase(name));
        }
        if (method != null && !method.isBlank()) {
            builder.and(qPermission.method.eq(method));
        }

        if (dateFrom != null) {
            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
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
}
