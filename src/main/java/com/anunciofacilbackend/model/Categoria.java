package com.anunciofacilbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 100)
    @Column(unique = true)
    private String nombreCategoria;

    // Relación con Publicaciones
    @OneToMany(mappedBy = "categoria")
    private List<Publicacion> publicaciones;
}
