package com.example.bookverse.controller;

import com.example.bookverse.domain.User;
import com.example.bookverse.domain.request.ReqLoginDTO;
import com.example.bookverse.domain.response.user.ResLoginDTO;
import com.example.bookverse.domain.response.user.UserDTO;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.exception.global.InvalidUsernameOrPassword;
import com.example.bookverse.service.UserService;
import com.example.bookverse.util.SecurityUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Value("${bookverse.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

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
            // Lưu thông tin vào response
            User user = this.userService.fetchUserByUsername(reqLoginDTO.getUsername());
            resLoginDTO.setUser(modelMapper.map(user, UserDTO.class));
            // Tạo accessToken
            String accessToken = this.securityUtil.createAccessToken(reqLoginDTO.getUsername(), resLoginDTO);
            resLoginDTO.setAccessToken(accessToken);
            String refreshToken = this.securityUtil.createRefreshToken(reqLoginDTO.getUsername(), resLoginDTO);
            // update user
            this.userService.updateRefreshToken(reqLoginDTO.getUsername(), refreshToken);
            // set cookie
            ResponseCookie springCookie = ResponseCookie.from("refresh_token", refreshToken).httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(resLoginDTO);
        }catch (Exception e) {
            throw new InvalidUsernameOrPassword("Invalid username or password");
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody User user) throws Exception {
        User newUser = this.userService.register(user);
        UserDTO userDTO = this.modelMapper.map(newUser, UserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ) throws Exception {
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String username = decodedToken.getSubject();
        User currentUser = this.userService.fetchUserByUsername(username);
        // check email valid
        if (!this.userService.checkUsernameAnhRefreshToken(username, refreshToken)) {
            throw new IdInvalidException("Refresh Token invalid");
        }
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setUser(modelMapper.map(currentUser, UserDTO.class));
        // Tạo accessToken
        String accessToken = this.securityUtil.createAccessToken(currentUser.getUsername(), resLoginDTO);
        resLoginDTO.setAccessToken(accessToken);
        String newRefreshToken = this.securityUtil.createRefreshToken(currentUser.getUsername(), resLoginDTO);
        // update user
        this.userService.updateRefreshToken(currentUser.getUsername(), newRefreshToken);
        // set cookie
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", newRefreshToken).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<UserDTO> getAccount() {
        String username = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.fetchUserByUsername(username);
        UserDTO userDTO = this.modelMapper.map(currentUser, UserDTO.class);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ResLoginDTO> logout() throws Exception {
        String username = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : " ";
        // check access token
        if (username.equals(" ")) {
            throw new IdInvalidException("Access Token invalid");
        }
        // update refresh token = null
        this.userService.updateRefreshToken(username, null);
        // remove refresh token in cookie
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", null).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).build();
    }
}