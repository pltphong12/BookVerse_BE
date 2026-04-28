package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Book;
import com.example.bookverse.dto.criteria.CriteriaFilterBook;
import com.example.bookverse.dto.criteria.CriteriaFilterProduct;
import com.example.bookverse.dto.response.ResPagination;

public interface BookService {

    Book create(Book book) throws Exception;

    Book update(Book book) throws Exception;

    Book fetchBookById(long id) throws Exception;

    List<Book> fetchAllBooks() throws Exception;

    List<Book> fetchTop5BooksByCreatedAt() throws Exception;

    ResPagination fetchAllBooksWithPaginationAndFilter(CriteriaFilterBook criteriaFilterBook, Pageable pageable) throws Exception;

    ResPagination fetchAllBooksWithPaginationAndFilter(CriteriaFilterProduct criteriaFilterProduct, Pageable pageable) throws Exception;

    ResPagination searchForClient(String keyword, Pageable pageable) throws Exception;

    List<String> suggestTitlesForClient(String keyword);

    void delete(long id) throws Exception;
}
