package com.example.bookverse.service;

import com.example.bookverse.domain.Book;

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

    // Delete a book by id
    void delete(long id) throws Exception;
}
