package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.QRole;
import com.example.bookverse.domain.Role;
import com.example.bookverse.repository.custom.RoleRepositoryCustom;
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
public class RoleRepositoryImpl implements RoleRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Role> filter(String name, LocalDate dateFrom, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRole qRole = QRole.role;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (name != null && !name.isBlank()) {
            builder.and(qRole.name.containsIgnoreCase(name));
        }

        if (dateFrom != null) {
            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
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
}
