package com.example.bookverse.config;

import java.util.List;

import com.example.bookverse.elasticsearch.BookDocument;
import com.example.bookverse.repository.BookSearchRepository;
import com.example.bookverse.service.BookEmbeddingService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.bookverse.domain.Book;
import com.example.bookverse.repository.BookRepository;

@Component
public class BookSearchIndexer {
    private final BookRepository bookRepository;
    private final BookSearchRepository bookSearchRepository;
    private final BookEmbeddingService bookEmbeddingService;

    public BookSearchIndexer(BookRepository bookRepository, BookSearchRepository bookSearchRepository, BookEmbeddingService bookEmbeddingService) {
        this.bookRepository = bookRepository;
        this.bookSearchRepository = bookSearchRepository;
        this.bookEmbeddingService = bookEmbeddingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void syncBooksToSearchIndex() {
        List<Book> allBooks = bookRepository.findAllWithAuthorsAndCategory();
        List<BookDocument> docs = allBooks.stream()
                .map(this::toSearchDocumentWithEmbedding)
                .toList();
        bookSearchRepository.saveAll(docs);
    }

    private BookDocument toSearchDocumentWithEmbedding(Book book) {
        BookDocument document = BookDocument.fromBook(book);
        document.setEmbedding(bookEmbeddingService.embed(document.getRagContent()));
        return document;
    }
}
