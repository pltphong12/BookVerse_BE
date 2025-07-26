package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CategoryRepositoryCustom {
    Page<Category> filter(String name, LocalDate dateFrom, Pageable pageable);
}
