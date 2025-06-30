package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PublisherRepositoryCustom {
    Page<Publisher> filter(String name, LocalDate dateFrom, Pageable pageable);

}
