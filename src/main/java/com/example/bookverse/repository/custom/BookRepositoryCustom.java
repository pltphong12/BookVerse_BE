package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface BookRepositoryCustom {
    Page<Book> filter(String title, long publisherId, long authorId, long categoryId, LocalDate dateFrom, Pageable pageable);
}
