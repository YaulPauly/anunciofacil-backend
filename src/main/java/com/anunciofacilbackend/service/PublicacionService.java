package com.anunciofacilbackend.service;

import com.anunciofacilbackend.model.Publicacion;
import com.anunciofacilbackend.model.Usuario;
import com.anunciofacilbackend.model.Categoria;
import com.anunciofacilbackend.repository.PublicacionRepository;
import com.anunciofacilbackend.repository.UsuarioRepository;
import com.anunciofacilbackend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    // ========== CONSULTAS ==========

    public List<Publicacion> getAllPublicaciones() {
        return publicacionRepository.findAll();
    }

    public Publicacion getPublicacionById(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));
    }

    public List<Publicacion> getPublicacionesActivas() {
        return publicacionRepository.findByEstado("Activo");
    }

    public List<Publicacion> getPublicacionesByCategoria(Long idCategoria) {
        return publicacionRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public List<Publicacion> getPublicacionesActivasByCategoria(Long idCategoria) {
        return publicacionRepository.findByCategoriaIdCategoriaAndEstado(idCategoria, "Activo");
    }

    public List<Publicacion> getPublicacionesByUsuario(Long idUsuario) {
        return publicacionRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<Publicacion> buscarPorTitulo(String titulo) {
        return publicacionRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Publicacion> buscarPorLocacion(String locacion) {
        return publicacionRepository.findByLocacionContainingIgnoreCase(locacion);
    }

    // ========== CREACIÓN ==========

    @Transactional
    public Publicacion createPublicacion(Publicacion publicacion) {
        Usuario usuario = usuarioRepository.findById(publicacion.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Categoria categoria = categoriaRepository.findById(publicacion.getCategoria().getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        publicacion.setUsuario(usuario);
        publicacion.setCategoria(categoria);
        publicacion.setEstado("Activo");
        publicacion.setFechaPublicacion(LocalDateTime.now());

        return publicacionRepository.save(publicacion);
    }

    // ========== ACTUALIZACIÓN ==========

    @Transactional
    public Publicacion updatePublicacion(Long id, Publicacion publicacionActualizada) {
        Publicacion publicacion = getPublicacionById(id);

        if (publicacionActualizada.getTitulo() != null) {
            publicacion.setTitulo(publicacionActualizada.getTitulo());
        }
        if (publicacionActualizada.getContenido() != null) {
            publicacion.setContenido(publicacionActualizada.getContenido());
        }
        if (publicacionActualizada.getLocacion() != null) {
            publicacion.setLocacion(publicacionActualizada.getLocacion());
        }
        if (publicacionActualizada.getImagenes() != null) {
            publicacion.setImagenes(publicacionActualizada.getImagenes());
        }
        if (publicacionActualizada.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(publicacionActualizada.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            publicacion.setCategoria(categoria);
        }

        return publicacionRepository.save(publicacion);
    }

    @Transactional
    public Publicacion desactivarPublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id);
        publicacion.setEstado("Inactivo");
        return publicacionRepository.save(publicacion);
    }

    @Transactional
    public Publicacion activarPublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id);
        publicacion.setEstado("Activo");
        return publicacionRepository.save(publicacion);
    }

    // ========== ELIMINACIÓN ==========

    @Transactional
    public void deletePublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id);
        publicacionRepository.delete(publicacion);
    }

    // ========== VERIFICACIÓN ==========

    public boolean esPropietario(Long idPublicacion, Long idUsuario) {
        Publicacion publicacion = getPublicacionById(idPublicacion);
        return publicacion.getUsuario().getIdUsuario().equals(idUsuario);
    }

    // ========== ESTADÍSTICAS ==========

    public long contarTotal() {
        return publicacionRepository.count();
    }

    public long contarActivas() {
        return publicacionRepository.findByEstado("Activo").size();
    }
}
