package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.example.bookverse.config.RedisCacheConfig;
import com.example.bookverse.dto.response.ResSupplierDTO;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.QSupplier;
import com.example.bookverse.domain.Supplier;
import com.example.bookverse.dto.criteria.CriteriaFilterSupplier;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.SupplierRepository;
import com.example.bookverse.service.SupplierService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class SupplierServiceImpl implements SupplierService{
    private final SupplierRepository supplierRepository;
    private final JPAQueryFactory queryFactory;
    private final ModelMapper mapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, JPAQueryFactory queryFactory,  ModelMapper mapper) {
        this.supplierRepository = supplierRepository;
        this.queryFactory = queryFactory;
        this.mapper = mapper;
    }

    @Override
    @CacheEvict(cacheNames = RedisCacheConfig.SUPPLIER, key = "'all'")
    public Supplier create(Supplier supplier) throws Exception {
        if (supplierRepository.existsByName(supplier.getName())) {
            throw new ExistDataException(supplier.getName() + " already exists");
        }
        return supplierRepository.save(supplier);
    }
    
    @Override
    @CacheEvict(cacheNames = RedisCacheConfig.SUPPLIER, key = "'all'")
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
    @Cacheable(cacheNames = RedisCacheConfig.SUPPLIER, key = "'all'")
    public List<ResSupplierDTO> fetchAllSuppliers() throws Exception {
        List<ResSupplierDTO> res = new ArrayList<>();
        List<Supplier> suppliers = supplierRepository.findAll();
        for (Supplier supplier : suppliers){
            ResSupplierDTO resSupplierDTO = mapper.map(supplier, ResSupplierDTO.class);
            res.add(resSupplierDTO);
        }
        return res;
    }

    public Page<Supplier> filter(CriteriaFilterSupplier criteriaFilterSupplier, Pageable pageable) {
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
    @CacheEvict(cacheNames = RedisCacheConfig.SUPPLIER, key = "'all'")
    public void delete(long id) throws Exception {
        supplierRepository.deleteById(id);
    }
    
    
}
