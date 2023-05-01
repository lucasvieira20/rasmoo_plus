package com.client.ws.rasmooplus.filter;

import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.repository.UserDetailsRepository;
import com.client.ws.rasmooplus.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class AuthenticationFilter extends OncePerRequestFilter {
    public AuthenticationFilter(TokenService tokenService, UserDetailsRepository userDetailsRepository) {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getBearerToken(request);

        filterChain.doFilter(request, response);

    }

    private String getBearerToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (Objects.isNull(token) ||!token.startsWith("Bearer")) {
            return null;
        }

        return token;
    }
}
