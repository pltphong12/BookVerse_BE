package com.example.bookverse.service;

import java.util.List;

import com.example.bookverse.dto.response.ResCategoryDTO;
import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.Category;
import com.example.bookverse.dto.criteria.CriteriaFilterCategory;
import com.example.bookverse.dto.response.ResPagination;

public interface CategoryService {
    // Create a role
    Category create(Category category) throws Exception;

    // Update a role
    Category update(Category category) throws Exception;

    // Fetch a role
    Category fetchCategoryById(long id) throws Exception;

    // Fetch all role
    List<ResCategoryDTO> fetchAllCategory() throws Exception;

    // Fetch all category
    ResPagination fetchAllCategoriesWithPaginationAndFilter(CriteriaFilterCategory criteriaFilterCategory, Pageable pageable) throws Exception;

    // Delete a role
    void delete(long id) throws Exception;
}
