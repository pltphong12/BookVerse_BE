package com.example.bookverse.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.bookverse.elasticsearch.BookDocument;

public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {
}
