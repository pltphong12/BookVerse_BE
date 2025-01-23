package com.example.bookverse.controller;

import com.example.bookverse.domain.User;
import com.example.bookverse.domain.request.ReqLoginDTO;
import com.example.bookverse.domain.response.user.ResLoginDTO;
import com.example.bookverse.domain.response.user.UserDTO;
import com.example.bookverse.exception.global.InvalidUsernameOrPassword;
import com.example.bookverse.service.UserService;
import com.example.bookverse.util.SecurityUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          UserService userService, ModelMapper modelMapper,
                          SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) throws InvalidUsernameOrPassword {
        try {
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            // Gửi thông tin input gồm username và password vào security
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
            // Xác thực người dùng => cần viết hàm loadUserByUsername
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // Thông tin được thêm vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = this.userService.fetchUserByUsername(reqLoginDTO.getUsername());
            resLoginDTO.setUser(modelMapper.map(user, UserDTO.class));
            String accessToken = this.securityUtil.createToken(reqLoginDTO.getUsername(), resLoginDTO);
            resLoginDTO.setAccessToken(accessToken);
            return ResponseEntity.status(HttpStatus.OK).body(resLoginDTO);
        }catch (Exception e) {
            throw new InvalidUsernameOrPassword("Invalid username or password");
        }
    }
}
