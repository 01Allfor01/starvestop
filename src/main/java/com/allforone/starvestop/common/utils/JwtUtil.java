package com.allforone.starvestop.common.utils;

import com.allforone.starvestop.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${JWT_SECRET_KEY}")
    private String secretKeyString;

    private SecretKey key;
    private JwtParser parser;

    @PostConstruct
    public void init() {

        byte[] bytes = Decoders.BASE64.decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.parser = Jwts.parser()
                .verifyWith(this.key)
                .build();
    }

    public String generateToken(String username, String userEmail, Long userId, UserRole userRole) {

        Date now = new Date();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .claim("userEmail", userEmail)
                .claim("userRole", userRole.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TOKEN_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {

        if (token == null || token.isBlank()) return false;

        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }

        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }

    public String extractUserEmail(String token) {
        return extractAllClaims(token).get("userEmail", String.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    public UserRole extractUserRole(String token) {
        String userRoleString = extractAllClaims(token).get("userRole", String.class);

        return UserRole.valueOf(userRoleString);
    }
}
