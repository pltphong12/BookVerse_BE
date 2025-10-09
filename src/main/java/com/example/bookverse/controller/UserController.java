package com.example.bookverse.controller;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookverse.domain.User;
import com.example.bookverse.domain.criteria.CriteriaFilterUser;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.user.UserDTO;
import com.example.bookverse.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user) throws Exception {
        User newUser = userService.create(user);
        // Convert DTO
        UserDTO DTO = modelMapper.map(newUser, UserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(DTO);
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user) throws Exception {
        User updateUser = userService.update(user);
        // Convert DTO
        UserDTO DTO = modelMapper.map(updateUser, UserDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(DTO);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) throws Exception {
        User user = this.userService.fetchUserById(id);
        // Convert DTO
        UserDTO DTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(DTO);
    }

    @GetMapping("/users")
    public ResponseEntity<ResPagination> getUsersWithPagination(Pageable pageable) throws Exception {
        ResPagination users = this.userService.fetchAllUsersWithPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/users/search")
    public ResponseEntity<ResPagination> getUsersWithPaginationAndFilter(
            @ModelAttribute CriteriaFilterUser criteriaFilterUser,
            Pageable pageable) throws Exception {
        ResPagination users = this.userService.fetchAllUsersWithPaginationAndFilter(criteriaFilterUser, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<UserDTO>> getUsers() throws Exception {
//        List<User> users = this.userService.fetchAllUsers();
//        // Convert DTO
//        List<UserDTO> DTOs = new ArrayList<>();
//        for (User user : users) {
//            UserDTO DTO = modelMapper.map(user, UserDTO.class);
//            DTOs.add(DTO);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(DTOs);
//    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws Exception {
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
