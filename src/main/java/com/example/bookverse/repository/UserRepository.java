package com.example.bookverse.repository;

import com.example.bookverse.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByUsernameAndRefreshToken(String username, String refreshToken);
    Page<User> findAll (Pageable pageable);
}
