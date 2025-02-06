package com.example.bookverse.controller;

import com.example.bookverse.domain.Book;
import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.response.category.ResCategoryDTO;
import com.example.bookverse.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
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

    // Delete a category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) throws Exception {
        this.categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
