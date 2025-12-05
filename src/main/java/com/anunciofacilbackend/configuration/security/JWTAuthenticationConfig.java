package com.anunciofacilbackend.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class JWTAuthenticationConfig extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(Constants.LOGIN_URL); // ruta de login
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> creds = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = creds.get("email");
            String password = creds.get("password");
            if (email == null) email = "";
            if (password == null) password = "";
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error al leer credenciales", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        String role = authResult.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USUARIO");

        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + Constants.EXPIRATION_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET.getBytes())
                .compact();

        response.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + Constants.TOKEN_PREFIX + token + "\"}");
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Autenticación fallida\"}");
        response.getWriter().flush();
    }
}
