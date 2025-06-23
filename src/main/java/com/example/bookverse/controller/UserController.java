package com.example.bookverse.controller;

import com.example.bookverse.domain.User;
import com.example.bookverse.domain.response.ResPagination;
import com.example.bookverse.domain.response.user.UserDTO;
import com.example.bookverse.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            @RequestParam(required = false) String name,
            @RequestParam(name = "role_id",defaultValue = "0") long roleId,
            @RequestParam(name = "date_from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            Pageable pageable) throws Exception {
        ResPagination users = this.userService.fetchAllUsersWithPaginationAndFilter(name, roleId, dateFrom, pageable);
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
