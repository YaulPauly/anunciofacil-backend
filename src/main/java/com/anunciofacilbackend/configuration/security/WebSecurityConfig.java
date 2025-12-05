package com.anunciofacilbackend.configuration.security;

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
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configuración básica
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Constants.LOGIN_URL).permitAll()
                        //.requestMatchers("/api/admin/generate-hash").permitAll() //permite esa ruta para la prueba
                        .anyRequest().authenticated()
                );

        // Crear el filtro de autenticación JWT
        JWTAuthenticationConfig jwtAuthenticationFilter = new JWTAuthenticationConfig(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl(Constants.LOGIN_URL);

        // Añadir filtros
        http.addFilter(jwtAuthenticationFilter);
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
