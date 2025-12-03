package com.anunciofacilbackend.security.servicio;

import com.anunciofacilbackend.model.Usuario;
import com.anunciofacilbackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetalleUsuarioServicio implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en TU base de datos
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // 2. Convertimos tu Usuario a un UserDetails de Spring Security
        return new User(
                usuario.getEmail(),
                usuario.getContraseña(),
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().name())) // Rol
        );
    }


}
