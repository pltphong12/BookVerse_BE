package com.example.bookverse.util;

import com.example.bookverse.domain.response.ResLoginDTO;
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

    public String createAccessToken(String username, ResLoginDTO userDTO) {
        ResLoginDTO.UserInSideToken token = new ResLoginDTO.UserInSideToken();
        token.setId(userDTO.getUser().getId());
        token.setUsername(userDTO.getUser().getUsername());
        token.setFullName(userDTO.getUser().getFullName());
        token.setRole(userDTO.getUser().getRole().getName());
        // Lay thoi gian thuc
        Instant now = Instant.now();
        // Tinh thoi gian het han token
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);
        // Set permission
        List<String> listAuthority = new ArrayList<>();
        // Add permissions từ database
        if (userDTO.getUser().getRole() != null && userDTO.getUser().getRole().getPermissions() != null) {
            for (var permission : userDTO.getUser().getRole().getPermissions()) {
                listAuthority.add(permission.getName());
            }
        }
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(username)
                .claim("user", token)
                .claim("permissions", listAuthority)
                .build();
        // Thuat toan ma hoa
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

    public String createRefreshToken(String username, ResLoginDTO userDTO) {
        ResLoginDTO.UserInSideToken token = new ResLoginDTO.UserInSideToken();
        token.setId(userDTO.getUser().getId());
        token.setUsername(userDTO.getUser().getUsername());
        token.setFullName(userDTO.getUser().getFullName());
        // Lay thoi gian thuc
        Instant now = Instant.now();
        // Tinh thoi gian het han token
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(username)
                .claim("user", token)
                .build();
        // Thuat toan ma hoa
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
}
