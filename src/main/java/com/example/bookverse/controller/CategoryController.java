package com.example.bookverse.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.criteria.CriteriaFilterCategory;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.category.ResCategoryDTO;
import com.example.bookverse.service.CategoryService;

import jakarta.validation.Valid;

@RequestMapping("/api/v1")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create category
    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws Exception {
        Category newCategory = this.categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    // Update a category
    @PutMapping("/categories")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws Exception {
        Category updatedCategory = this.categoryService.update(category);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    // Fetch a category
    @GetMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW_BY_ID')")
    public ResponseEntity<ResCategoryDTO> getCategory(@PathVariable long id) throws Exception {
        Category category = this.categoryService.fetchCategoryById(id);
        ResCategoryDTO resCategoryDTO = ResCategoryDTO.from(category);
        return ResponseEntity.status(HttpStatus.OK).body(resCategoryDTO);
    }

    // Fetch all categories
    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW_ALL')")
    public ResponseEntity<List<ResCategoryDTO>> getAllCategories() throws Exception {
        List<Category> categories = this.categoryService.fetchAllCategory();
        List<ResCategoryDTO> res = new ArrayList<>();
        for (Category category : categories) {
            ResCategoryDTO resCategoryDTO = ResCategoryDTO.from(category);
            res.add(resCategoryDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Fetch all categories with filter and pagination
    @GetMapping("/categories/search")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW_ALL_WITH_PAGINATION_AND_FILTER')")
    public ResponseEntity<ResPagination> getAllCategoriesWithFilterAndPagination(
            @ModelAttribute CriteriaFilterCategory criteriaFilterCategory,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.categoryService.fetchAllCategoriesWithPaginationAndFilter(criteriaFilterCategory, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    // Delete a category
    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) throws Exception {
        this.categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
