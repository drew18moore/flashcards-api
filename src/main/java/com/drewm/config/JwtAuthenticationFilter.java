package com.drewm.config;

import com.drewm.exception.ResourceNotFoundException;
import com.drewm.model.User;
import com.drewm.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        boolean isAuthEndpoint = Objects.equals(requestURI.split("/")[3], "auth");
        final String jwt;
        final Integer userId;
        if (authHeader == null || !authHeader.startsWith("Bearer ") || isAuthEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        userId = Integer.valueOf(jwtService.extractUserId(jwt));
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.getUserByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (jwtService.isTokenValid(jwt, user)) {
                System.out.println("GET AUTHORITIES: " + user.getAuthorities());
                System.out.println("GET USERNAME: " + user.getUsername());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}