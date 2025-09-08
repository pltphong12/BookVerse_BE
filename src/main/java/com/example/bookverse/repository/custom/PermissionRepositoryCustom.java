package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PermissionRepositoryCustom {
    Page<Permission> filter(String name, String method, LocalDate dateFrom, Pageable pageable);

}
