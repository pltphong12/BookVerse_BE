package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface RoleRepositoryCustom {
    Page<Role> filter(String name, LocalDate dateFrom, Pageable pageable);

}
