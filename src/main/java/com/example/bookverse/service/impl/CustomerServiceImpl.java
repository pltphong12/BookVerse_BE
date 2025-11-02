package com.example.bookverse.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.QCustomer;
import com.example.bookverse.domain.User;
import com.example.bookverse.domain.criteria.CriteriaFilterCustomer;
import com.example.bookverse.domain.enums.CustomerLevel;
import com.example.bookverse.domain.request.ReqCustomerDTO;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.customer.ResCustomerDTO;
import com.example.bookverse.repository.CustomerRepository;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.repository.UserRepository;
import com.example.bookverse.service.CustomerService;
import com.example.bookverse.service.UserService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.example.bookverse.util.SecurityUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    private final EntityManager entityManager;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository,
            RoleRepository roleRepository, UserService userService,
            EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.entityManager = entityManager;
    }

    @Override
    public ResCustomerDTO create(ReqCustomerDTO reqCustomerDTO) throws Exception {
        // Check if the customer already exists
        if (customerRepository.existsByIdentityCard(reqCustomerDTO.getIdentityCard())) {
            throw new Exception("Customer already exists");
        }
        Customer customer = new Customer();
        customer.setIdentityCard(reqCustomerDTO.getIdentityCard());
        customer.setTotalOrder(0L);
        customer.setTotalSpending(BigDecimal.ZERO);
        customer.setCustomerLevel(CustomerLevel.BRONZE);

        User user = new User();
        user.setUsername(reqCustomerDTO.getUsername());
        user.setPassword(reqCustomerDTO.getPassword());
        user.setFullName(reqCustomerDTO.getFullName());
        user.setEmail(reqCustomerDTO.getEmail());
        user.setAddress(reqCustomerDTO.getAddress());
        user.setPhone(reqCustomerDTO.getPhone());
        user.setAvatar(reqCustomerDTO.getAvatar());
        user.setCreatedBy(
                SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "");
        user.setCreatedAt(Instant.now());
        user.setRole(roleRepository.findByName("CUSTOMER"));

        user = this.userService.create(user);
        customer.setUser(user);
        customer = customerRepository.save(customer);
        return ResCustomerDTO.fromCustomer(customer, user);
    }

    @Override
    public ResCustomerDTO update(ReqCustomerDTO reqCustomerDTO) throws Exception {
        Customer customer = FindObjectInDataBase.findByIdOrThrow(customerRepository, reqCustomerDTO.getId().get());
        if (reqCustomerDTO.getIdentityCard() != null
                && !reqCustomerDTO.getIdentityCard().equals(customer.getIdentityCard())) {
            customer.setIdentityCard(reqCustomerDTO.getIdentityCard());
        }
        if (reqCustomerDTO.getCustomerLevel() != null
                && !reqCustomerDTO.getCustomerLevel().equals(customer.getCustomerLevel())) {
            customer.setCustomerLevel(reqCustomerDTO.getCustomerLevel());
        }
        if (reqCustomerDTO.getFullName() != null
                && !reqCustomerDTO.getFullName().equals(customer.getUser().getFullName())) {
            customer.getUser().setFullName(reqCustomerDTO.getFullName());
        }
        if (reqCustomerDTO.getEmail() != null
                && !reqCustomerDTO.getEmail().equals(customer.getUser().getEmail())) {
            customer.getUser().setEmail(reqCustomerDTO.getEmail());
        }
        if (reqCustomerDTO.getPhone() != null
                && !reqCustomerDTO.getPhone().equals(customer.getUser().getPhone())) {
            customer.getUser().setPhone(reqCustomerDTO.getPhone());
        }
        if (reqCustomerDTO.getAvatar() != null
                && !reqCustomerDTO.getAvatar().equals(customer.getUser().getAvatar())) {
            customer.getUser().setAvatar(reqCustomerDTO.getAvatar());
        }
        customer = customerRepository.save(customer);
        return ResCustomerDTO.fromCustomer(customer, customer.getUser());
    }

    @Override
    public ResCustomerDTO fetchCustomerById(long id) throws Exception {
        Customer customer = FindObjectInDataBase.findByIdOrThrow(customerRepository, id);
        return ResCustomerDTO.fromCustomer(customer, customer.getUser());
    }

    private Page<Customer> filter(CriteriaFilterCustomer criteriaFilterCustomer, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCustomer qCustomer = QCustomer.customer;

        BooleanBuilder builder = new BooleanBuilder();
        // Filter
        if (criteriaFilterCustomer.getIdentityCard() != null && !criteriaFilterCustomer.getIdentityCard().isBlank()) {
            builder.and(qCustomer.identityCard.containsIgnoreCase(criteriaFilterCustomer.getIdentityCard()));
        }

        if (criteriaFilterCustomer.getCustomerLevel() != null && !criteriaFilterCustomer.getCustomerLevel().isBlank()) {
            builder.and(qCustomer.customerLevel.eq(CustomerLevel.valueOf(criteriaFilterCustomer.getCustomerLevel())));
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
    public ResPagination fetchAllCustomersWithPaginationAndFilter(CriteriaFilterCustomer criteriaFilterCustomer,
            Pageable pageable) throws Exception {
        Page<Customer> pageCustomer = this.filter(criteriaFilterCustomer, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageCustomer.getSize());

        mt.setPages(pageCustomer.getTotalPages());
        mt.setTotal(pageCustomer.getTotalElements());

        rs.setMeta(mt);
        List<Customer> customers = pageCustomer.getContent();
        List<ResCustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOS.add(ResCustomerDTO.fromCustomer(customer, customer.getUser()));
        }

        rs.setResult(customerDTOS);

        return rs;
    }

    @Override
    public void delete(long id) throws Exception {
        Customer customer = FindObjectInDataBase.findByIdOrThrow(customerRepository, id);
        customerRepository.delete(customer);
        userRepository.delete(customer.getUser());
    }
}
