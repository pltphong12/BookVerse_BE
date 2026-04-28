package com.example.bookverse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.bookverse.domain.BookSearchDocument;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookSearchDocument, Long> {
    Page<BookSearchDocument> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword,
            Pageable pageable);

    List<BookSearchDocument> findTop10ByTitleContainingIgnoreCaseOrderBySoldDesc(String titleKeyword);
}
