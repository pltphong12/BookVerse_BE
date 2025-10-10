package com.example.bookverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

        private final JwtConfiguration jwtConfiguration;

        public SecurityConfiguration(JwtConfiguration jwtConfiguration) {
                this.jwtConfiguration = jwtConfiguration;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                        CustomBearerTokenResolver customBearerTokenResolver,
                        CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
                String[] whiteList = {
                                "/",
                                "/storage/**",
                                "/api/v1/auth/login",
                                "/api/v1/auth/register",
                                "/api/v1/auth/refresh",
                };
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults())
                                .authorizeHttpRequests(request -> request
                                                .requestMatchers(whiteList).permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(AbstractHttpConfigurer::disable)
                                .oauth2ResourceServer((oauth2 -> oauth2
                                                .bearerTokenResolver(customBearerTokenResolver)
                                                // Handle Jwt in Header Exception
                                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                                .jwt(jwt -> jwt
                                                                .jwtAuthenticationConverter(jwtConfiguration
                                                                                .jwtAuthenticationConverter()))))
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler(customAccessDeniedHandler))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();
        }

}