package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.QCategory;
import com.example.bookverse.repository.custom.CategoryRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Category> filter(String title, LocalDate dateFrom, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCategory qCategory = QCategory.category;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (title != null && !title.isBlank()) {
            builder.and(qCategory.name.containsIgnoreCase(title));
        }

        if (dateFrom != null) {
            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qCategory.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Category> categories = queryFactory.selectFrom(qCategory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qCategory)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(categories, pageable, total);
    }
}
