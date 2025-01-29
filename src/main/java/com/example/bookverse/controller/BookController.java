package com.example.bookverse.controller;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.response.book.ResBookDTO;
import com.example.bookverse.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    // Create a book
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) throws Exception{
        Book newBook = this.bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    // Update a books
    @PutMapping("/books")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) throws Exception{
        Book updatedBook = this.bookService.update(book);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    // Fetch a book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<ResBookDTO> getBookById(@PathVariable Long id) throws Exception{
        Book book = this.bookService.fetchBookById(id);
        ResBookDTO resBookDTO = ResBookDTO.from(book);
        return ResponseEntity.status(HttpStatus.OK).body(resBookDTO);
    }

    // Fetch all books
    @GetMapping("/books")
    public ResponseEntity<List<ResBookDTO>> getAllBooks() throws Exception{
        List<Book> books = this.bookService.fetchAllBooks();
        List<ResBookDTO> resBookDTOS = new ArrayList<>();
        for (Book book : books) {
            ResBookDTO resBookDTO = ResBookDTO.from(book);
            resBookDTOS.add(resBookDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resBookDTOS);
    }

    // Delete a book by id
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws Exception{
        this.bookService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
