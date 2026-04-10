package com.example.bookverse.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bookverse.domain.QUser;
import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.User;
import com.example.bookverse.dto.criteria.CriteriaFilterUser;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.dto.response.ResUserDTO;
import com.example.bookverse.exception.global.ExistDataException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.repository.UserRepository;
import com.example.bookverse.service.UserService;
import com.example.bookverse.util.FindObjectInDataBase;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, ModelMapper modelMapper, JPAQueryFactory queryFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.queryFactory = queryFactory;
    }

    @Override
    public User create(User user) throws Exception {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new ExistDataException("Email " + user.getEmail() + " đã tồn tại");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        Role role = this.roleRepository.findById(user.getRole().getId()).orElse(null);
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws IdInvalidException {
        User userInDB = FindObjectInDataBase.findByIdOrThrow(userRepository, user.getId());
        if (user.getEmail() != null && !user.getEmail().equals(userInDB.getEmail())) {
            userInDB.setEmail(user.getEmail());
        }
        if (user.getFullName() != null && !user.getFullName().equals(userInDB.getFullName())) {
            userInDB.setFullName(user.getFullName());
        }
        if (user.getAddress() != null && !user.getAddress().equals(userInDB.getAddress())) {
            userInDB.setAddress(user.getAddress());
        }
        if (user.getPhone() != null && !user.getPhone().equals(userInDB.getPhone())) {
            userInDB.setPhone(user.getPhone());
        }
        if (user.getAvatar() != null && !user.getAvatar().equals(userInDB.getAvatar())) {
            userInDB.setAvatar(user.getAvatar());
        }
        if (user.getRole() != null) {
            userInDB.setRole(user.getRole());
        }
        return userRepository.save(userInDB);
    }

    @Override
    public User fetchUserById(long id) throws IdInvalidException {
        return FindObjectInDataBase.findByIdOrThrow(userRepository, id);
    }

    @Override
    public User fetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public List<User> fetchAllUsers() throws Exception {
        return this.userRepository.findAll();
    }

    public Page<User> filter(CriteriaFilterUser criteriaFilterUser, Pageable pageable) {
        QUser qUser = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (criteriaFilterUser.getEmail() != null && !criteriaFilterUser.getEmail().isBlank()) {
            builder.and(qUser.email.containsIgnoreCase(criteriaFilterUser.getEmail()));
        }
        if (criteriaFilterUser.getRoleId() != 0) {
            builder.and(qUser.role.id.eq(criteriaFilterUser.getRoleId()));
        }
        if (criteriaFilterUser.getDateFrom() != null) {
            Instant fromInstant = criteriaFilterUser.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(qUser.createdAt.goe(fromInstant));
        }
        // Query chính
        List<User> users = queryFactory.selectFrom(qUser)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Đếm số lượng kết quả
        long total = queryFactory.selectFrom(qUser)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public ResPagination fetchAllUsersWithPaginationAndFilter(CriteriaFilterUser criteriaFilterUser,
            Pageable pageable) {
        Page<User> pageUser = this.filter(criteriaFilterUser, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<User> users = pageUser.getContent();
        List<ResUserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            ResUserDTO userDTO = modelMapper.map(user, ResUserDTO.class);
            userDTOS.add(userDTO);
        }

        rs.setResult(userDTOS);

        return rs;
    }

    @Override
    public void delete(long id) throws IdInvalidException {
        FindObjectInDataBase.findByIdOrThrow(userRepository, id);
        this.userRepository.deleteById(id);
    }

    @Override
    public void updateRefreshToken(String email, String refreshToken) {
        User userInDB = this.userRepository.findByEmail(email);
        if (userInDB != null) {
            userInDB.setRefreshToken(refreshToken);
            this.userRepository.save(userInDB);
        }
    }

    @Override
    public User register(User user) throws Exception {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new ExistDataException("Email đã tồn tại");
        }
        Role role = this.roleRepository.findByName("CUSTOMER");
        user.setRole(role);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return this.userRepository.save(user);
    }

    @Override
    public boolean checkEmailAndRefreshToken(String email, String refreshToken) {
        return userRepository.existsByEmailAndRefreshToken(email, refreshToken);
    }
}
