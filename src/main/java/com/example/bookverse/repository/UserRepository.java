package com.example.bookverse.repository;

import com.example.bookverse.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByUsernameAndRefreshToken(String username, String refreshToken);
    Page<User> findAll (Pageable pageable);
}
