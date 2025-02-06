package com.example.bookverse.repository;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);
    List<Book> findByAuthors(List<Author> authors);
}
