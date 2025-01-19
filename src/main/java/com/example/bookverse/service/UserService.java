package com.example.bookverse.service;

import com.example.bookverse.domain.User;

import java.util.List;

public interface UserService {

    // Create a user
    User create(User user) throws Exception;

    // Update a user
    User update(User user) throws Exception;

    // Find a user by id
    User fetchUserById(long id) throws Exception;

    // Find a user by username
    User fetchUserByUsername(String username) throws Exception;

    // Find all user
    List<User> fetchAllUsers();

    // Delete a user by id
    void delete(long id) throws Exception;
}
