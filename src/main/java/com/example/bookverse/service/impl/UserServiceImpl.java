package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Role;
import com.example.bookverse.domain.User;
import com.example.bookverse.exception.user.ExistUsernameException;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.RoleRepository;
import com.example.bookverse.repository.UserRepository;
import com.example.bookverse.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) throws ExistUsernameException {
        // Check Username
        if (this.userRepository.existsByUsername(user.getUsername())) {
            throw new ExistUsernameException(user.getUsername() + " already exists");
        }

        // Save hashPassword
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save role
        Role role = this.roleRepository.findById(user.getRole().getId()).orElse(null);
        if (role != null) {
            user.setRole(role);
        }else {
            user.setRole(null);
        }

        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws IdInvalidException {
        User userInDB = this.userRepository.findById(user.getId()).orElse(null);
        if (userInDB == null) {
            throw new IdInvalidException("User not found");
        }
        else {
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
            return userRepository.save(userInDB);
        }
    }

    @Override
    public User fetchUserById(long id) throws IdInvalidException{
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IdInvalidException("User not found");
        }
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User fetchUserByUsername(String username){
        User user = this.userRepository.findByUsername(username);
        return this.userRepository.findByUsername(username);
    }

    @Override
    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void delete(long id) throws IdInvalidException{
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IdInvalidException("User not found");
        }
        this.userRepository.deleteById(id);
    }

}
