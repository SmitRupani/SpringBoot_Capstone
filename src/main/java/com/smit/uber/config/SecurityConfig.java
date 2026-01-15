package com.smit.uber.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ⚠️ Swagger paths MUST be first!
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                        .requestMatchers("/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        // API endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/driver/**").hasRole("DRIVER")
                        .requestMatchers("/api/v1/user/**").hasRole("USER")
                        .requestMatchers("/api/files/**").permitAll()
                        .requestMatchers("/api/test/notifications/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(rateLimitFilter, JwtFilter.class);

        return http.build();
    }
}