package com.example.bookverse.repository;

import com.example.bookverse.domain.Author;
import com.example.bookverse.repository.custom.AuthorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {
    boolean existsByName(String name);
}
