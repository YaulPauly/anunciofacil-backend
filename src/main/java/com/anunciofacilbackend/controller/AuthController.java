package com.anunciofacilbackend.controller;

import com.anunciofacilbackend.dto.UsuarioLoginDTO;
import com.anunciofacilbackend.dto.UsuarioRegisterDTO;
import com.anunciofacilbackend.model.Usuario;
import com.anunciofacilbackend.security.jwt.JwtUtil;
import com.anunciofacilbackend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;



    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRegisterDTO usuarioDTO) {
        // Lógica para registrar un nuevo usuario
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuarioDTO);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody UsuarioLoginDTO loginDTO) {
        // Lógica para autenticar al usuario
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            //Generar el token JWT
            String token = jwtUtil.generateToken(loginDTO.getEmail());

            Map<String, String> response = Collections.singletonMap("token", token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

    }




}
