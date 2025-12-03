package com.anunciofacilbackend.service;

import com.anunciofacilbackend.dto.UsuarioRegisterDTO;
import com.anunciofacilbackend.model.Usuario;
import com.anunciofacilbackend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(UsuarioRegisterDTO dto){
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("El email ya está registrado");
        }

        // Mapeo de DTO a entidad
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setApellidos(dto.getApellidos());
        nuevoUsuario.setEmail(dto.getEmail());
        //Pendiente a Encriptar
        nuevoUsuario.setContraseña(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setEstado(dto.getEstado());
        nuevoUsuario.setFechaRegistro(dto.getFechaRegistro());
        nuevoUsuario.setRol(dto.getRol());

        return usuarioRepository.save(nuevoUsuario);
        }

}
