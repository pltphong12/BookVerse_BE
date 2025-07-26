package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Author;
import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.author.ResAuthorDTO;
import com.example.bookverse.domain.response.category.ResCategoryDTO;
import com.example.bookverse.exception.category.ExistCategoryNameException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.CategoryRepository;
import com.example.bookverse.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) throws Exception {
        if (this.categoryRepository.existsByName(category.getName())) {
            throw new ExistCategoryNameException(category.getName() + " already exist");
        }
        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) throws Exception {
        Category categoryInDB = this.categoryRepository.findById(category.getId()).orElse(null);
        if (categoryInDB == null) {
            throw new IdInvalidException("Category not found");
        }
        else {
            if (category.getName() != null && !category.getName().equals(categoryInDB.getName())) {
                categoryInDB.setName(category.getName());
            }
            if (category.getDescription() != null && !category.getDescription().equals(categoryInDB.getDescription())) {
                categoryInDB.setDescription(category.getDescription());
            }
            return categoryRepository.save(categoryInDB);
        }
    }

    @Override
    public Category fetchCategoryById(long id) throws Exception {
        if (!this.categoryRepository.existsById(id)) {
            throw new IdInvalidException("Category not found");
        }
        return this.categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> fetchAllCategory() throws Exception {
        return this.categoryRepository.findAll();
    }

    @Override
    public ResPagination fetchAllCategoriesWithPaginationAndFilter(String name, LocalDate dateFrom, Pageable pageable) throws Exception{
        Page<Category> pageCategory = this.categoryRepository.filter(name, dateFrom, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageCategory.getSize());

        mt.setPages(pageCategory.getTotalPages());
        mt.setTotal(pageCategory.getTotalElements());

        rs.setMeta(mt);

        List<Category> categories = pageCategory.getContent();
        List<ResCategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categories) {
            ResCategoryDTO categoryDTO = ResCategoryDTO.from(category);
            categoryDTOS.add(categoryDTO);
        }

        rs.setResult(categoryDTOS);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        if (!this.categoryRepository.existsById(id)) {
            throw new IdInvalidException("Category not found");
        }
        this.categoryRepository.deleteById(id);
    }
}
