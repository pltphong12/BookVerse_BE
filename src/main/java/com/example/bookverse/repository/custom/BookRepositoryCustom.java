package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<Book> filter(Pageable pageable);
}
