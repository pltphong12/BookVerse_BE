package com.example.bookverse.repository;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.repository.custom.PermissionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, PermissionRepositoryCustom {
    boolean existsByName(String name);
}
