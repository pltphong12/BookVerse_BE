package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.example.bookverse.config.RedisCacheConfig;
import com.example.bookverse.dto.response.ResPublisherDTO;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.Publisher;
import com.example.bookverse.domain.QPublisher;
import com.example.bookverse.dto.criteria.CriteriaFilterPublisher;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.PublisherRepository;
import com.example.bookverse.service.PublisherService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final JPAQueryFactory queryFactory;
    private final ModelMapper mapper;

    public PublisherServiceImpl(PublisherRepository publisherRepository, JPAQueryFactory queryFactory,  ModelMapper mapper) {
        this.publisherRepository = publisherRepository;
        this.queryFactory = queryFactory;
        this.mapper = mapper;
    }

    // Create
    @Override
    @CacheEvict(cacheNames = RedisCacheConfig.PUBLISHER, key = "'all'")
    public Publisher create(Publisher publisher) throws Exception {
        if (this.publisherRepository.existsByName(publisher.getName())) {
            throw new ExistDataException(publisher.getName() + " already exists");
        }
        return publisherRepository.save(publisher);
    }

    // Update
    @Override
    @CacheEvict(cacheNames = RedisCacheConfig.PUBLISHER, key = "'all'")
    public Publisher update(Publisher publisher) throws Exception {
        Publisher publisherInDB = FindObjectInDataBase.findByIdOrThrow(publisherRepository, publisher.getId());
        if (publisher.getName() != null && !publisher.getName().equals(publisherInDB.getName())) {
            publisherInDB.setName(publisher.getName());
        }
        if (publisher.getAddress() != null && !publisher.getAddress().equals(publisherInDB.getAddress())) {
            publisherInDB.setAddress(publisher.getAddress());
        }
        if (publisher.getEmail() != null && !publisher.getEmail().equals(publisherInDB.getEmail())) {
            publisherInDB.setEmail(publisher.getEmail());
        }
        if (publisher.getPhone() != null && !publisher.getPhone().equals(publisherInDB.getPhone())) {
            publisherInDB.setPhone(publisher.getPhone());
        }
        if (publisher.getDescription() != null && !publisher.getDescription().equals(publisherInDB.getDescription())) {
            publisherInDB.setDescription(publisher.getDescription());
        }
        if (publisher.getImage() != null && !publisher.getImage().equals(publisherInDB.getImage())) {
            publisherInDB.setImage(publisher.getImage());
        }
        return publisherRepository.save(publisherInDB);
    }

    //Fetch
    @Override
    public Publisher fetchPublisherById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(publisherRepository, id);
    }

    // Fetch all
    @Override
    @Cacheable(cacheNames = RedisCacheConfig.PUBLISHER, key = "'all'", unless = "#result == null or #result.empty")
    public List<ResPublisherDTO> fetchAllPublisher() throws Exception {
        List<ResPublisherDTO> res = new ArrayList<>();
        List<Publisher> publishers = this.publisherRepository.findAll();
        for (Publisher publisher : publishers) {
            ResPublisherDTO publisherDTO = mapper.map(publisher, ResPublisherDTO.class);
            res.add(publisherDTO);
        }
        return res;
    }

    public Page<Publisher> filter(CriteriaFilterPublisher criteriaFilterPublisher, Pageable pageable) {
        QPublisher qPublisher = QPublisher.publisher;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterPublisher.getName() != null && !criteriaFilterPublisher.getName().isBlank()) {
            builder.and(qPublisher.name.containsIgnoreCase(criteriaFilterPublisher.getName()));
        }

        if (criteriaFilterPublisher.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterPublisher.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qPublisher.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Publisher> publishers = queryFactory.selectFrom(qPublisher)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qPublisher)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(publishers, pageable, total);
    }

    @Override
    @Cacheable(cacheNames = RedisCacheConfig.PUBLISHER, key = "'allWithPaginationAndFilter:' + #criteriaFilterPublisher + '_' + #pageable", unless = "#result == null or #result.empty")
    public ResPagination fetchAllPublisherWithPaginationAndFilter(CriteriaFilterPublisher criteriaFilterPublisher, Pageable pageable) throws Exception {
        Page<Publisher> pagePublisher = this.filter(criteriaFilterPublisher, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pagePublisher.getSize());

        mt.setPages(pagePublisher.getTotalPages());
        mt.setTotal(pagePublisher.getTotalElements());

        rs.setMeta(mt);

        List<Publisher> publishers = pagePublisher.getContent();

        rs.setResult(publishers);

        return rs;
    }

    // Delete
    @Override
    @CacheEvict(cacheNames = RedisCacheConfig.PUBLISHER, key = "'all'")
    public void delete(long id) throws Exception {
        FindObjectInDataBase.findByIdOrThrow(publisherRepository, id);
        this.publisherRepository.deleteById(id);
    }
}
