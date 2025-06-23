package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.QAuthor;
import com.example.bookverse.repository.custom.AuthorRepositoryCustom;
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

public class AuthorRepositoryImpl implements AuthorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Author> filter(String title, String nationality, LocalDate dateFrom, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAuthor qAuthor = QAuthor.author;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (title != null && !title.isBlank()) {
            builder.and(qAuthor.name.containsIgnoreCase(title));
        }

        if (nationality != null && !nationality.isBlank()) {
            builder.and(qAuthor.nationality.containsIgnoreCase(nationality));
        }

        if (dateFrom != null) {
            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qAuthor.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Author> authors = queryFactory.selectFrom(qAuthor)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qAuthor)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(authors, pageable, total);
    }
}
