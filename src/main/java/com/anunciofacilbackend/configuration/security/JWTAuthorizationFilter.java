package com.anunciofacilbackend.configuration.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
        String header = request.getHeader(Constants.HEADER_STRING);
        if (header == null || !header.startsWith(Constants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(Constants.TOKEN_PREFIX, "");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Constants.SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String user = claims.getSubject();
            String roleClaim = (claims.get("role") != null) ? claims.get("role").toString() : "ROLE_USUARIO";

            if (user != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(roleClaim))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            // token inválido; limpiamos contexto y dejamos pasar para que los endpoints protejidos respondan 401
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
