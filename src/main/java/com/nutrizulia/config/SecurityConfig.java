package com.nutrizulia.config;

import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.features.auth.jwt.JwtAuthenticationFilter;
import com.nutrizulia.features.auth.jwt.JwtEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final JwtEntryPoint jwtEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers(
                                        ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_SIGN_IN,
                                        ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_SIGN_UP,
                                        ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_FORGOT_PASSWORD,
                                        ApiConstants.AUTH_BASE_URL + ApiConstants.AUTH_SIGN_IN_ADMIN,
                                        "/api/v1/reports/**"
                                ).permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}