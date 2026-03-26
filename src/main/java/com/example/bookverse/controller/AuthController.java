package com.example.bookverse.controller;

import com.example.bookverse.domain.User;
import com.example.bookverse.dto.request.ReqLoginDTO;
import com.example.bookverse.dto.response.ResLoginDTO;
import com.example.bookverse.dto.response.ResUserDTO;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.exception.global.InvalidEmailOrPassword;
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
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) throws InvalidEmailOrPassword {
        try {
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(reqLoginDTO.getEmail(), reqLoginDTO.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = this.userService.fetchUserByEmail(reqLoginDTO.getEmail());
            resLoginDTO.setUser(modelMapper.map(user, ResUserDTO.class));

            String accessToken = this.securityUtil.createAccessToken(reqLoginDTO.getEmail(), resLoginDTO);
            resLoginDTO.setAccessToken(accessToken);
            String refreshToken = this.securityUtil.createRefreshToken(reqLoginDTO.getEmail(), resLoginDTO);

            this.userService.updateRefreshToken(reqLoginDTO.getEmail(), refreshToken);

            ResponseCookie springCookie = ResponseCookie.from("refresh_token", refreshToken).httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(resLoginDTO);
        } catch (Exception e) {
            throw new InvalidEmailOrPassword("Email hoặc mật khẩu không đúng");
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ResUserDTO> register(@Valid @RequestBody User user) throws Exception {
        User newUser = this.userService.register(user);
        ResUserDTO userDTO = this.modelMapper.map(newUser, ResUserDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ) throws Exception {
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();
        User currentUser = this.userService.fetchUserByEmail(email);

        if (!this.userService.checkEmailAndRefreshToken(email, refreshToken)) {
            throw new IdInvalidException("Refresh Token không hợp lệ");
        }

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setUser(modelMapper.map(currentUser, ResUserDTO.class));

        String accessToken = this.securityUtil.createAccessToken(currentUser.getEmail(), resLoginDTO);
        resLoginDTO.setAccessToken(accessToken);
        String newRefreshToken = this.securityUtil.createRefreshToken(currentUser.getEmail(), resLoginDTO);

        this.userService.updateRefreshToken(currentUser.getEmail(), newRefreshToken);

        ResponseCookie springCookie = ResponseCookie.from("refresh_token", newRefreshToken).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, springCookie.toString()).body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<ResUserDTO> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.fetchUserByEmail(email);
        ResUserDTO userDTO = this.modelMapper.map(currentUser, ResUserDTO.class);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ResLoginDTO> logout() throws Exception {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : " ";
        if (email.equals(" ")) {
            throw new IdInvalidException("Access Token không hợp lệ");
        }
        this.userService.updateRefreshToken(email, null);
        ResponseCookie springCookie = ResponseCookie.from("refresh_token", null).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).build();
    }
}