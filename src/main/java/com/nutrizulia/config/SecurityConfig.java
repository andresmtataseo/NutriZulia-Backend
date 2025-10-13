package com.nutrizulia.config;

import com.nutrizulia.common.util.ApiConstants;
import com.nutrizulia.features.auth.jwt.JwtEntryPoint;
import com.nutrizulia.features.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
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
                                        "/healt"
                                ).permitAll()
                                // Endpoint público para descarga del manual de la aplicación
                                .requestMatchers(ApiConstants.PUBLIC_BASE_URL + ApiConstants.PUBLIC_MANUAL_APP).permitAll()
                                // Permitir temporalmente el acceso al dashboard sin autenticación para pruebas en entorno de desarrollo
                                .requestMatchers(ApiConstants.DASHBOARD_BASE_URL + "/**").permitAll()
                                // Asegurar explícitamente que cada endpoint del dashboard esté permitido (por si falla el patrón anterior)
                                .requestMatchers(ApiConstants.DASHBOARD_BASE_URL + ApiConstants.DASHBOARD_CONSULTATIONS_PER_MONTH).permitAll()
                                .requestMatchers(ApiConstants.DASHBOARD_BASE_URL + ApiConstants.DASHBOARD_ACTIVE_INSTITUTIONS_BY_MUNICIPALITY).permitAll()
                                .requestMatchers(ApiConstants.DASHBOARD_BASE_URL + ApiConstants.DASHBOARD_ACTIVE_USERS_BY_INSTITUTION).permitAll()
                                .requestMatchers(ApiConstants.DASHBOARD_BASE_URL + ApiConstants.DASHBOARD_AGE_GROUP_DISTRIBUTION).permitAll()
                                .requestMatchers(ApiConstants.DASHBOARD_BASE_URL + ApiConstants.DASHBOARD_NUTRITIONAL_STATUS_BY_AGE_GROUP).permitAll()
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