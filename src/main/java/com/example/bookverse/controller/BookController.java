package com.example.bookverse.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.criteria.CriteriaFilterBook;
import com.example.bookverse.domain.response.ResBookDTO;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Create a book
    @PostMapping("/books")
    @PreAuthorize("hasAuthority('BOOK_CREATE')")
    public ResponseEntity<ResBookDTO> createBook(@Valid @RequestBody Book book) throws Exception {
        Book newBook = this.bookService.create(book);
        ResBookDTO resBookDTO = ResBookDTO.from(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(resBookDTO);
    }

    // Update a books
    @PutMapping("/books")
    @PreAuthorize("hasAuthority('BOOK_UPDATE')")
    public ResponseEntity<ResBookDTO> updateBook(@RequestBody Book book) throws Exception {
        Book updatedBook = this.bookService.update(book);
        ResBookDTO resBookDTO = ResBookDTO.from(updatedBook);
        return ResponseEntity.status(HttpStatus.OK).body(resBookDTO);
    }

    // Fetch a book by id
    @GetMapping("/books/{id}")
    @PreAuthorize("hasAuthority('BOOK_VIEW_BY_ID')")
    public ResponseEntity<ResBookDTO> getBookById(@PathVariable Long id) throws Exception {
        Book book = this.bookService.fetchBookById(id);
        ResBookDTO resBookDTO = ResBookDTO.from(book);
        return ResponseEntity.status(HttpStatus.OK).body(resBookDTO);
    }

    // Fetch all books
    @GetMapping("/books")
    @PreAuthorize("hasAuthority('BOOK_VIEW_ALL')")
    public ResponseEntity<List<ResBookDTO>> getAllBooks() throws Exception {
        List<Book> books = this.bookService.fetchAllBooks();
        List<ResBookDTO> resBookDTOS = new ArrayList<>();
        for (Book book : books) {
            ResBookDTO resBookDTO = ResBookDTO.from(book);
            resBookDTOS.add(resBookDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resBookDTOS);
    }

    // Fetch top 5 books by created at
    @GetMapping("/books/top-5-latest")
    public ResponseEntity<List<ResBookDTO>> getTop5LatestBooks() throws Exception {
        List<Book> books = this.bookService.fetchTop5BooksByCreatedAt();
        List<ResBookDTO> resBookDTOS = new ArrayList<>();
        for (Book book : books) {
            ResBookDTO resBookDTO = ResBookDTO.from(book);
            resBookDTOS.add(resBookDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resBookDTOS);
    }

    @GetMapping("/books/search")
    @PreAuthorize("hasAuthority('BOOK_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getAllBooksWithPagination(
            @ModelAttribute CriteriaFilterBook criteriaFilterBook,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.bookService.fetchAllBooksWithPaginationAndFilter(criteriaFilterBook,
                pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    // Delete a book by id
    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasAuthority('BOOK_DELETE')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws Exception {
        this.bookService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
