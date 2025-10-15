package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.QSupplier;
import com.example.bookverse.domain.Supplier;
import com.example.bookverse.domain.criteria.CriteriaFilterSupplier;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.SupplierRepository;
import com.example.bookverse.service.SupplierService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class SupplierServiceImpl implements SupplierService{
    private final SupplierRepository supplierRepository;
    private final EntityManager entityManager;

    public SupplierServiceImpl(SupplierRepository supplierRepository, EntityManager entityManager) {
        this.supplierRepository = supplierRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Supplier create(Supplier supplier) throws Exception {
        if (supplierRepository.existsByName(supplier.getName())) {
            throw new ExistDataException(supplier.getName() + " already exists");
        }
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier update(Supplier supplier) throws Exception {
        Supplier supplierInDB = FindObjectInDataBase.findByIdOrThrow(supplierRepository, supplier.getId());
        if (supplier.getName() != null && !supplier.getName().equals(supplierInDB.getName())) {
            if (supplierRepository.existsByName(supplier.getName())) {
                throw new ExistDataException(supplier.getName() + " already exists");
            }
            supplierInDB.setName(supplier.getName());
        }
        if (supplier.getAddress() != null && !supplier.getAddress().equals(supplierInDB.getAddress())) {
            supplierInDB.setAddress(supplier.getAddress());
        }
        if (supplier.getEmail() != null && !supplier.getEmail().equals(supplierInDB.getEmail())) {
            supplierInDB.setEmail(supplier.getEmail());
        }
        if (supplier.getPhone() != null && !supplier.getPhone().equals(supplierInDB.getPhone())) {
            supplierInDB.setPhone(supplier.getPhone());
        }
        if (supplier.getDescription() != null && !supplier.getDescription().equals(supplierInDB.getDescription())) {
            supplierInDB.setDescription(supplier.getDescription());
        }
        if (supplier.getImage() != null && !supplier.getImage().equals(supplierInDB.getImage())) {
            supplierInDB.setImage(supplier.getImage());
        }
        return supplierRepository.save(supplierInDB);
    }

    @Override
    public Supplier fetchSupplierById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(supplierRepository, id);
    }
    
    @Override
    public List<Supplier> fetchAllSuppliers() throws Exception {
        return supplierRepository.findAll();
    }

    public Page<Supplier> filter(CriteriaFilterSupplier criteriaFilterSupplier, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QSupplier qSupplier = QSupplier.supplier;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterSupplier.getName() != null && !criteriaFilterSupplier.getName().isBlank()) {
            builder.and(qSupplier.name.containsIgnoreCase(criteriaFilterSupplier.getName()));
        }

        if (criteriaFilterSupplier.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterSupplier.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qSupplier.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Supplier> suppliers = queryFactory.selectFrom(qSupplier)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qSupplier)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(suppliers, pageable, total);
    }

    @Override
    public ResPagination fetchAllSuppliersWithPaginationAndFilter(CriteriaFilterSupplier criteriaFilterSupplier, Pageable pageable) throws Exception {
        Page<Supplier> pageSupplier = this.filter(criteriaFilterSupplier, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageSupplier.getSize());

        mt.setPages(pageSupplier.getTotalPages());
        mt.setTotal(pageSupplier.getTotalElements());

        rs.setMeta(mt);

        List<Supplier> suppliers = pageSupplier.getContent();
        rs.setResult(suppliers);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        supplierRepository.deleteById(id);
    }
    
    
}
