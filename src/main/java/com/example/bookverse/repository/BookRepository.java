package com.example.bookverse.repository;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {
    boolean existsByTitle(String title);
    List<Book> findByAuthors(List<Author> authors);
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.category")
    List<Book> findAllWithAuthorsAndCategory();
    List<Book> findTop5ByOrderByCreatedAtDesc();
}
