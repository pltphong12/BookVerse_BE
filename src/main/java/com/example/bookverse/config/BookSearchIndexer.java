package com.example.bookverse.config;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.BookSearchDocument;
import com.example.bookverse.repository.BookRepository;
import com.example.bookverse.repository.BookSearchRepository;

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
        List<BookSearchDocument> documents = this.bookRepository.findAll().stream()
                .map(this::toSearchDocument)
                .toList();
        this.bookSearchRepository.saveAll(documents);
    }

    private BookSearchDocument toSearchDocument(Book book) {
        return new BookSearchDocument(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getPrice(),
                book.getDiscount(),
                book.getSold(),
                book.getImage(),
                book.getCreatedAt());
    }
}
