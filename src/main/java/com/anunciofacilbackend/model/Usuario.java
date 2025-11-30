package com.anunciofacilbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 150)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email no válido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contraseña;

    private String fotoPerfil;

    @Size(min = 8, max = 8, message = "El DNI debe contener 8 dígitos")
    private String dni;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    // 🔹 Nuevo campo ROL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;  // ADMIN o USUARIO

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Publicacion> publicaciones;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
}
