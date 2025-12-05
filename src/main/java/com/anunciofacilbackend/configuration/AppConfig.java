package com.anunciofacilbackend.configuration;

import com.anunciofacilbackend.model.Usuario;
import com.anunciofacilbackend.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AppConfig {
    private final UsuarioRepository usuarioRepository;

    public AppConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Usuario> opt = usuarioRepository.findByEmail(username);
            if (opt.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado con email: " + username);
            }
            Usuario u = opt.get();

            String roleName = (u.getRol() != null) ? u.getRol().name() : "USUARIO";

            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPassword())
                    .roles(roleName)
                    .disabled(!"ACTIVO".equalsIgnoreCase(u.getEstado()))
                    .build();
        };
    }

}
