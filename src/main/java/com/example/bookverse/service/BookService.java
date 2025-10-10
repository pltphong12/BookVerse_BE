package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.criteria.CriteriaFilterBook;
import com.example.bookverse.domain.response.ResPagination;

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
    ResPagination fetchAllBooksWithPaginationAndFilter(CriteriaFilterBook criteriaFilterBook, Pageable pageable) throws Exception;

    // Delete a book by id
    void delete(long id) throws Exception;
}
