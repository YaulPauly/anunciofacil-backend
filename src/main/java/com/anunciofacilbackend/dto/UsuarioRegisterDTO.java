package com.anunciofacilbackend.dto;

import com.anunciofacilbackend.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegisterDTO {

    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String fotoPerfil;
    private String estado = "ACTIVO";
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private Rol rol = Rol.USUARIO;



}
