package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.QCustomer;
import com.example.bookverse.domain.criteria.CriteriaFilterCustomer;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.customer.ResCustomerDTO;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.repository.CustomerRepository;
import com.example.bookverse.service.CustomerService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Customer create(Customer customer) throws Exception {
        if (this.customerRepository.existsByUsername(customer.getUsername())) {
            throw new ExistDataException(customer.getUsername() + " đã tồn tại");
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer) throws Exception {
        Customer customerInDB = FindObjectInDataBase.findByIdOrThrow(customerRepository, customer.getId());
        if (customer.getUsername() != null && !customer.getUsername().equals(customerInDB.getUsername())) {
            if (this.customerRepository.existsByUsername(customer.getUsername())) {
                throw new ExistDataException(customer.getUsername() + " đã tồn tại");
            }
            customerInDB.setUsername(customer.getUsername());
        }
        if (customer.getFullName() != null && !customer.getFullName().equals(customerInDB.getFullName())) {
            customerInDB.setFullName(customer.getFullName());
        }
        if (customer.getEmail() != null && !customer.getEmail().equals(customerInDB.getEmail())) {
            customerInDB.setEmail(customer.getEmail());
        }
        if (customer.getAddress() != null && !customer.getAddress().equals(customerInDB.getAddress())) {
            customerInDB.setAddress(customer.getAddress());
        }
        if (customer.getPhone() != null && !customer.getPhone().equals(customerInDB.getPhone())) {
            customerInDB.setPhone(customer.getPhone());
        }
        if (customer.getAvatar() != null && !customer.getAvatar().equals(customerInDB.getAvatar())) {
            customerInDB.setAvatar(customer.getAvatar());
        }
        if (customer.getRole() != null && !customer.getRole().equals(customerInDB.getRole())) {
            customerInDB.setRole(customer.getRole());
        }
        return customerRepository.save(customerInDB);
    }

    @Override
    public Customer fetchCustomerById(long id) throws Exception {
        return FindObjectInDataBase.findByIdOrThrow(customerRepository, id);
    }

    @Override
    public List<Customer> fetchAllCustomers() throws Exception {
        return customerRepository.findAll();
    }
    
    public Page<Customer> filterCustomersUserName(CriteriaFilterCustomer criteriaFilterCustomer, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCustomer qCustomer = QCustomer.customer;

        BooleanBuilder builder = new BooleanBuilder();

        if (criteriaFilterCustomer.getUsername() != null && !criteriaFilterCustomer.getUsername().isBlank()) {
            builder.and(qCustomer.username.containsIgnoreCase(criteriaFilterCustomer.getUsername()));
        }
        if (criteriaFilterCustomer.getRoleId() != 0) {
            builder.and(qCustomer.role.id.eq(criteriaFilterCustomer.getRoleId()));
        }
        if (criteriaFilterCustomer.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterCustomer.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qCustomer.createdAt.goe(fromInstant));
        }
        // Query chính
        List<Customer> customers = queryFactory.selectFrom(qCustomer)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qCustomer)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(customers, pageable, total);
    }

    @Override
    public ResPagination fetchAllCustomersWithPaginationAndFilter(CriteriaFilterCustomer criteriaFilterCustomer, Pageable pageable) throws Exception {
        Page<Customer> pageCustomer = this.filterCustomersUserName(criteriaFilterCustomer, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageCustomer.getSize());
        mt.setPages(pageCustomer.getTotalPages());
        mt.setTotal(pageCustomer.getTotalElements());

        rs.setMeta(mt);

        List<Customer> customers = pageCustomer.getContent();
        List<ResCustomerDTO> customerDTOS = new ArrayList<>(customers.size());
        for (Customer customer : customers) {
            ResCustomerDTO customerDTO = modelMapper.map(customer, ResCustomerDTO.class);
            customerDTOS.add(customerDTO);
        }

        rs.setResult(customerDTOS);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        customerRepository.deleteById(id);
    }
}
