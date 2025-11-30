package com.anunciofacilbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Size(min = 2, max = 500)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion;

    // FK Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // FK Publicación
    @ManyToOne
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;
}
