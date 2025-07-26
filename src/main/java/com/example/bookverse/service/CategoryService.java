package com.example.bookverse.service;

import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.response.ResPagination;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CategoryService {
    // Create a role
    Category create(Category category) throws Exception;

    // Update a role
    Category update(Category category) throws Exception;

    // Fetch a role
    Category fetchCategoryById(long id) throws Exception;

    // Fetch all role
    List<Category> fetchAllCategory() throws Exception;

    // Fetch all category
    ResPagination fetchAllCategoriesWithPaginationAndFilter(String name, LocalDate dateFrom, Pageable pageable) throws Exception;

    // Delete a role
    void delete(long id) throws Exception;
}
