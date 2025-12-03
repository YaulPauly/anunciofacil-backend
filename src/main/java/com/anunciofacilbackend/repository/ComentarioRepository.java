package com.anunciofacilbackend.repository;

import com.anunciofacilbackend.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacionIdPublicacion(Long idPublicacion);
    List<Comentario> findByUsuarioIdUsuario(Long idUsuario);
    long countByPublicacionIdPublicacion(Long idPublicacion);
}
