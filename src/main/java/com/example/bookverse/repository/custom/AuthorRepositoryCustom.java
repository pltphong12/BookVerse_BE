package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AuthorRepositoryCustom {
    Page<Author> filter(String title, String nationality, LocalDate dateFrom, Pageable pageable);
}
