package com.anunciofacilbackend.security.jwt;

import com.anunciofacilbackend.security.servicio.DetalleUsuarioServicio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFiltroAutenticacion extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final DetalleUsuarioServicio detalleUsuarioServicio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String usuarioEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        // Extraer el token JWT del encabezado
        jwt = authHeader.substring(7);
        usuarioEmail = jwtUtil.extractUsername(jwt);

        if(usuarioEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Cargamos los detalles del usuario desde la BD
            UserDetails userDetails = this.detalleUsuarioServicio.loadUserByUsername(usuarioEmail);

            if (jwtUtil.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);;
            }
        }
        filterChain.doFilter(request,response);
    }
}
