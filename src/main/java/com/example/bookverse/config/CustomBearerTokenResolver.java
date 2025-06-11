package com.example.bookverse.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomBearerTokenResolver implements BearerTokenResolver {

    private final List<String> whitelist = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/",
            "/api/v1/auth/refresh"
    );

    private final BearerTokenResolver defaultResolver = new org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Bỏ qua kiểm tra token nếu request nằm trong whitelist
        if (whitelist.contains(path)) {
            return null;
        }
        // Ngược lại thì vẫn parse token như bình thường
        return defaultResolver.resolve(request);
    }
}