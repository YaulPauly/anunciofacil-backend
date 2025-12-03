package com.anunciofacilbackend.repository;

import com.anunciofacilbackend.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // Buscar por categoría
    List<Publicacion> findByCategoriaIdCategoria(Long idCategoria);

    // Buscar por usuario
    List<Publicacion> findByUsuarioIdUsuario(Long idUsuario);

    // Buscar por estado
    List<Publicacion> findByEstado(String estado);

    // Buscar por locación
    List<Publicacion> findByLocacionContainingIgnoreCase(String locacion);

    // Buscar por categoría y estado
    List<Publicacion> findByCategoriaIdCategoriaAndEstado(Long idCategoria, String estado);

    // Buscar por usuario y estado
    List<Publicacion> findByUsuarioIdUsuarioAndEstado(Long idUsuario, String estado);
}