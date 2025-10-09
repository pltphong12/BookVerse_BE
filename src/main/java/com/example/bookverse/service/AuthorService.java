package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.criteria.CriteriaFilterAuthor;
import com.example.bookverse.domain.response.ResPagination;

public interface AuthorService {
    // Create an author
    Author create(Author author) throws Exception;

    // Update an author
    Author update(Author author) throws Exception;

    // Fetch an author by id
    Author fetchAuthorById(long id) throws Exception;

    // Fetch all authors
    List<Author> fetchAllAuthors() throws Exception;

    // Fetch all authors with pagination and filter
    ResPagination fetchAllAuthorsWithPaginationAndFilter(CriteriaFilterAuthor criteriaFilterAuthor, Pageable pageable) throws Exception;

    // Delete an author
    void delete(long id) throws Exception;
}
