package com.example.bookverse.repository;

import com.example.bookverse.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>, QuerydslPredicateExecutor<Publisher> {
    boolean existsByName(String name);
}
