package com.example.bookverse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.bookverse.domain.User;
import com.example.bookverse.domain.criteria.CriteriaFilterUser;
import com.example.bookverse.domain.response.ResPagination;

public interface UserService {

    // Create a user
    User create(User user) throws Exception;

    // Update a user
    User update(User user) throws Exception;

    // Find a user by id
    User fetchUserById(long id) throws Exception;

    // Find a user by username
    User fetchUserByUsername(String username);

    // Find all user 
    List<User> fetchAllUsers() throws Exception;

    // Find all user with pagination and filter
    ResPagination fetchAllUsersWithPaginationAndFilter(CriteriaFilterUser criteriaFilterUser, Pageable pageable) throws Exception;

    // Delete a user by id
    void delete(long id) throws Exception;

    // Update refreshToken
    void updateRefreshToken(String username, String refreshToken) throws Exception;

    // Register
    User register(User user) throws Exception;

    // Check username and RefreshToken
    boolean checkUsernameAnhRefreshToken(String username, String refreshToken) throws Exception;
}
