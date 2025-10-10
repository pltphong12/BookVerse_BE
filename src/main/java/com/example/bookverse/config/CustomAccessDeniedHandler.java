package com.example.bookverse.config;

import com.example.bookverse.domain.response.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        RestResponse<Object> res = new RestResponse<>();
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setError("Access Denied");
        res.setMessage("Không có quyền truy cập vào tài nguyên: " + request.getRequestURI());

        mapper.writeValue(response.getWriter(), res);
    }
}