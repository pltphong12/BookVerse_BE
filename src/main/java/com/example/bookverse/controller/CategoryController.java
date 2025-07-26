package com.example.bookverse.controller;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.category.ResCategoryDTO;
import com.example.bookverse.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create category
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws Exception {
        Category newCategory = this.categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    // Update a category
    @PutMapping("/categories")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws Exception {
        Category updatedCategory = this.categoryService.update(category);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    // Fetch a category
    @GetMapping("/categories/{id}")
    public ResponseEntity<ResCategoryDTO> getCategory(@PathVariable long id) throws Exception {
        Category category = this.categoryService.fetchCategoryById(id);
        ResCategoryDTO resCategoryDTO = ResCategoryDTO.from(category);
        return ResponseEntity.status(HttpStatus.OK).body(resCategoryDTO);
    }

    // Fetch all categories
    @GetMapping("/categories")
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
    public ResponseEntity<ResPagination> getAllCategoriesWithFilterAndPagination(
            @RequestParam(required = false) String name,
            @RequestParam(name = "date_from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            Pageable pageable) throws Exception {
        ResPagination resPagination = this.categoryService.fetchAllCategoriesWithPaginationAndFilter(name, dateFrom, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    // Delete a category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) throws Exception {
        this.categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
