package com.example.bookverse.util;

import com.example.bookverse.dto.response.ResLoginDTO;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${bookverse.jwt.base64-secret}")
    private String jwtKey;

    @Value("${bookverse.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${bookverse.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public String createAccessToken(String email, ResLoginDTO userDTO) {
        ResLoginDTO.UserInSideToken token = new ResLoginDTO.UserInSideToken();
        token.setId(userDTO.getUser().getId());
        token.setEmail(userDTO.getUser().getEmail());
        token.setFullName(userDTO.getUser().getFullName());
        token.setRole(userDTO.getUser().getRole().getName());
        token.setCustomerId(userDTO.getUser().getCustomerId());

        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        List<String> listAuthority = new ArrayList<>();
        if (userDTO.getUser().getRole() != null && userDTO.getUser().getRole().getPermissions() != null) {
            for (var permission : userDTO.getUser().getRole().getPermissions()) {
                listAuthority.add(permission.getName());
            }
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", token)
                .claim("permissions", listAuthority)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

    public String createRefreshToken(String email, ResLoginDTO userDTO) {
        ResLoginDTO.UserInSideToken token = new ResLoginDTO.UserInSideToken();
        token.setId(userDTO.getUser().getId());
        token.setEmail(userDTO.getUser().getEmail());
        token.setFullName(userDTO.getUser().getFullName());
        token.setCustomerId(userDTO.getUser().getCustomerId());

        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", token)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String refreshToken) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(refreshToken);
        } catch (Exception e) {
            System.out.println(">>> JWT error: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;

    }

    /**
     * {@code customerId} trong claim {@code user} của access token (sau đăng nhập / refresh).
     */
    public static Optional<Long> getCurrentCustomerId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return Optional.empty();
        }
        Object userClaim = jwt.getClaim("user");
        if (!(userClaim instanceof Map<?, ?> map)) {
            return Optional.empty();
        }
        Object raw = map.get("customerId");
        if (raw == null) {
            return Optional.empty();
        }
        if (raw instanceof Number n) {
            return Optional.of(n.longValue());
        }
        return Optional.empty();
    }
}
