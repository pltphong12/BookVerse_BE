package com.example.bookverse.repository;

import com.example.bookverse.domain.User;
import com.example.bookverse.repository.custom.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByUsernameAndRefreshToken(String username, String refreshToken);
    Page<User> findAll (Pageable pageable);
}
