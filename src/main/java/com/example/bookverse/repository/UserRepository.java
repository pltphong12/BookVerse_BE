package com.example.bookverse.repository;

import com.example.bookverse.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndRefreshToken(String email, String refreshToken);

    Page<User> findAll (Pageable pageable);
}
