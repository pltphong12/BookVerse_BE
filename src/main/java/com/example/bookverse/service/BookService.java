package com.example.bookverse.service;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.response.ResPagination;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    // Create a book
    Book create(Book book) throws Exception;

    // Update a book
    Book update(Book book) throws Exception;

    // Fetch a book by id
    Book fetchBookById(long id) throws Exception;

    // Fetch all book
    List<Book> fetchAllBooks() throws Exception;

    // Fetch all book with pagination and filter
    ResPagination fetchAllBooksWithPaginationAndFilter(Pageable pageable) throws Exception;

    // Delete a book by id
    void delete(long id) throws Exception;
}
