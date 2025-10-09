package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Category;
import com.example.bookverse.domain.QCategory;
import com.example.bookverse.domain.criteria.CriteriaFilterCategory;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.category.ResCategoryDTO;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.CategoryRepository;
import com.example.bookverse.service.CategoryService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EntityManager entityManager) {
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Category create(Category category) throws Exception {
        if (this.categoryRepository.existsByName(category.getName())) {
            throw new ExistDataException(category.getName() + " already exist");
        }
        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) throws Exception {
        Category categoryInDB = FindObjectInDataBase.findByIdOrThrow(categoryRepository, category.getId());
        if (category.getName() != null && !category.getName().equals(categoryInDB.getName())) {
            categoryInDB.setName(category.getName());
        }
        if (category.getDescription() != null && !category.getDescription().equals(categoryInDB.getDescription())) {
            categoryInDB.setDescription(category.getDescription());
        }
        return categoryRepository.save(categoryInDB);
    }

    @Override
    public Category fetchCategoryById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(categoryRepository, id);
    }

    @Override
    public List<Category> fetchAllCategory() throws Exception {
        return this.categoryRepository.findAll();
    }

    public Page<Category> filter(CriteriaFilterCategory criteriaFilterCategory, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCategory qCategory = QCategory.category;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterCategory.getName() != null && !criteriaFilterCategory.getName().isBlank()) {
            builder.and(qCategory.name.containsIgnoreCase(criteriaFilterCategory.getName()));
        }

        if (criteriaFilterCategory.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterCategory.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qCategory.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Category> categories = queryFactory.selectFrom(qCategory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qCategory)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(categories, pageable, total);
    }

    @Override
    public ResPagination fetchAllCategoriesWithPaginationAndFilter(CriteriaFilterCategory criteriaFilterCategory, Pageable pageable) throws Exception{
        Page<Category> pageCategory = this.filter(criteriaFilterCategory, pageable);
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
        FindObjectInDataBase.findByIdOrThrow(categoryRepository, id);
        this.categoryRepository.deleteById(id);
    }
}
