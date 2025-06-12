package com.example.bookverse.repository.custom;

import com.example.bookverse.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserRepositoryCustom {
    Page<User> filterUsersName(String username, long id, LocalDate dateFrom, Pageable pageable);
}
