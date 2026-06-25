package com.example.bookverse.repository;

import com.example.bookverse.domain.Permission;
import com.example.bookverse.domain.Role;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
    boolean existsByName(String name);

    Role findByName(@NotBlank(message = "name isn't blank") String name);

    List<Role> findAllByPermissions(List<Permission> permissions);
}
