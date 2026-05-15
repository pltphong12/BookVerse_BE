package com.example.bookverse.config;

import java.util.List;

import com.example.bookverse.elasticsearch.BookDocument;
import com.example.bookverse.repository.BookSearchRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.bookverse.domain.Book;
import com.example.bookverse.repository.BookRepository;

@Component
public class BookSearchIndexer {
    private final BookRepository bookRepository;
    private final BookSearchRepository bookSearchRepository;

    public BookSearchIndexer(BookRepository bookRepository, BookSearchRepository bookSearchRepository) {
        this.bookRepository = bookRepository;
        this.bookSearchRepository = bookSearchRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void syncBooksToSearchIndex() {
        List<Book> allBooks = bookRepository.findAllWithAuthorsAndCategory();
        List<BookDocument> docs = allBooks.stream()
            .map(BookDocument::fromBook)
            .toList();
        bookSearchRepository.saveAll(docs);
    }
}
