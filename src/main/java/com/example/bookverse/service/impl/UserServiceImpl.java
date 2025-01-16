package com.example.bookverse.service.impl;

import com.example.bookverse.domain.User;
import com.example.bookverse.repository.UserRepository;
import com.example.bookverse.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User userInDB = this.userRepository.findById(user.getId()).orElse(null);
        if (userInDB == null) {
            return null;
        }
        else {
            if (user.getUsername() != null && !user.getUsername().equals(userInDB.getUsername())) {
                userInDB.setUsername(user.getUsername());
            }
            return userRepository.save(userInDB);
        }
    }

    @Override
    public User fetchUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User fetchUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void delete(long id) {
        this.userRepository.deleteById(id);
    }

}
