package com.example.bookverse.repository;

import com.example.bookverse.domain.Role;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    Role findByName(@NotBlank(message = "name isn't blank") String name);
}
