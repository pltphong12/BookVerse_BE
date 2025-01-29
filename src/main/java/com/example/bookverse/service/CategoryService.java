package com.example.bookverse.service;

import com.example.bookverse.domain.Category;

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

    // Delete a role
    void delete(long id) throws Exception;
}
