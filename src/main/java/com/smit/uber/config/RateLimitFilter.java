package com.smit.uber.config;

import com.smit.uber.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitService rateLimitService;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String user = "anonymous";

        if(SecurityContextHolder.getContext().getAuthentication() != null){
            user = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        boolean allowed = rateLimitService.allowRequest(user);

        if(!allowed){
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write("Rate limit exceeded. Try again later.");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
