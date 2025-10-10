package com.example.bookverse.repository;

import com.example.bookverse.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, QuerydslPredicateExecutor<Permission> {
    boolean existsByName(String name);
    Permission findByName(String name);
}
