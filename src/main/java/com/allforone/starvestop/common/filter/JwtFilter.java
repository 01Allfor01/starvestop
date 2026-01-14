package com.allforone.starvestop.common.filter;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.utils.JwtUtil;
import com.allforone.starvestop.domain.user.enums.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final Set<String> EXCLUDED_URIS = Set.of(
            "/auth/signup",
            "/auth/signin",
            "/coupons",
            "/accommodations",
            "/oauth2",
            "/login/oauth2",
            "/infra",
            "/success.html",
            "/fail.html"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return EXCLUDED_URIS.contains(uri)
                || uri.startsWith("/reservations/one/")
                || uri.startsWith("/payments/");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰");
            return;
        }

        Long userId = jwtUtil.extractUserId(token);
        String userEmail = jwtUtil.extractUserEmail(token);
        String username = jwtUtil.extractUsername(token);
        UserRole userRole = jwtUtil.extractUserRole(token);

        AuthUser authUser = AuthUser.of(userId, userEmail, username, userRole);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null, List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()))));

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(JwtUtil.BEARER_PREFIX)) {
            return header.substring(JwtUtil.BEARER_PREFIX.length());
        }

        if (request.getCookies() == null) return null;

        for (var cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                String value = cookie.getValue();
                if (value.startsWith(JwtUtil.BEARER_PREFIX)) {
                    return value.substring(JwtUtil.BEARER_PREFIX.length());
                }
                return value;
            }
        }

        return null;
    }
}