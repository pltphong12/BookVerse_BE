package com.example.bookverse.repository.impl;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.QBook;
import com.example.bookverse.domain.QUser;
import com.example.bookverse.domain.User;
import com.example.bookverse.repository.custom.BookRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Book> filter(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBook qBook = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
//        if (username != null && !username.isBlank()) {
//            builder.and(qUser.fullName.containsIgnoreCase(username));
//        }
//        if (roleId != 0) {
//            builder.and(qUser.role.id.eq(roleId));
//        }
//        if (dateFrom != null) {
//            Instant fromInstant = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
//            builder.and(qUser.createdAt.goe(fromInstant));
//        }
        // Query chính
        List<Book> books = queryFactory.selectFrom(qBook)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qBook)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(books, pageable, total);
    }
}
