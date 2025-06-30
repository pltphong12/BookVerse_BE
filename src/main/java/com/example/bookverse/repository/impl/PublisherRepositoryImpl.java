package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.QPublisher;
import com.example.bookverse.repository.custom.PublisherRepositoryCustom;
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
public class PublisherRepositoryImpl implements PublisherRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Publisher> filter(String name, LocalDate dateFrom, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPublisher qPublisher = QPublisher.publisher;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (name != null && !name.isBlank()) {
            builder.and(qPublisher.name.containsIgnoreCase(name));
        }

        if (dateFrom != null) {
            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qPublisher.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Publisher> publishers = queryFactory.selectFrom(qPublisher)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qPublisher)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(publishers, pageable, total);
    }
}
