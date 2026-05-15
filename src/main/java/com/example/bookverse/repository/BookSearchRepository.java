package com.example.bookverse.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.bookverse.elasticsearch.BookDocument;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {
}
