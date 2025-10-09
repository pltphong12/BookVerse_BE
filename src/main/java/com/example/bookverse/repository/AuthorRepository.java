package com.example.bookverse.repository;

import com.example.bookverse.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, QuerydslPredicateExecutor<Author> {
    boolean existsByName(String name);
}
