package com.example.bookverse.controller;

import com.example.bookverse.domain.request.ReqLoginDTO;
import com.example.bookverse.domain.response.user.ResLoginDTO;
import com.example.bookverse.exception.global.InvalidUsernameOrPassword;
import com.example.bookverse.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) throws InvalidUsernameOrPassword {
        try {
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            // Gửi thông tin input gồm username và password vào security
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
            // Xác thực người dùng => cần viết hàm loadUserByUsername
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            resLoginDTO.setUsername(authentication.getName());
            return ResponseEntity.status(HttpStatus.OK).body(resLoginDTO);
        }catch (Exception e) {
            throw new InvalidUsernameOrPassword("Invalid username or password");
        }
    }
}
