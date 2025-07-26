package com.example.bookverse.repository;

import com.example.bookverse.domain.Category;
import com.example.bookverse.repository.custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    boolean existsByName(String name);
}
