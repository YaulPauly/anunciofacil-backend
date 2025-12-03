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
@RequiredArgsConstructor  // Lombok genera el constructor automáticamente
public class PublicacionService {

    // ========== INYECCIÓN DE DEPENDENCIAS ==========
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;



    public List<Publicacion> getAllPublicaciones() {
        return publicacionRepository.findAll();
    }


    /**
     * Obtener publicación por ID
     * @param id ID de la publicación
     * @return Publicación encontrada
     * @throws RuntimeException si no existe
     */
    public Publicacion getPublicacionById(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));
    }


    /**
     * Obtener todas las publicaciones ACTIVAS (visibles públicamente)
     * @return Lista de publicaciones activas
     */
    public List<Publicacion> getPublicacionesActivas() {
        return publicacionRepository.findByEstado("Activo");
    }


    /**
     * Obtener todas las publicaciones INACTIVAS
     * @return Lista de publicaciones inactivas
     */
    public List<Publicacion> getPublicacionesInactivas() {
        return publicacionRepository.findByEstado("Inactivo");
    }


    /**
     * Obtener publicaciones por categoría
     * @param idCategoria ID de la categoría
     * @return Lista de publicaciones de esa categoría
     */
    public List<Publicacion> getPublicacionesByCategoria(Long idCategoria) {
        // Verificar que la categoría existe
        categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

        return publicacionRepository.findByCategoriaIdCategoria(idCategoria);
    }


    /**
     * Obtener publicaciones ACTIVAS por categoría
     * @param idCategoria ID de la categoría
     * @return Lista de publicaciones activas de esa categoría
     */
    public List<Publicacion> getPublicacionesActivasByCategoria(Long idCategoria) {
        // Verificar que la categoría existe
        categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

        return publicacionRepository.findByCategoriaIdCategoriaAndEstado(idCategoria, "Activo");
    }


    /**
     * Obtener todas las publicaciones de un usuario (sus anuncios)
     * @param idUsuario ID del usuario
     * @return Lista de publicaciones del usuario
     */
    public List<Publicacion> getPublicacionesByUsuario(Long idUsuario) {
        // Verificar que el usuario existe
        usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        return publicacionRepository.findByUsuarioIdUsuario(idUsuario);
    }


    /**
     * Obtener publicaciones activas de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de publicaciones activas del usuario
     */
    public List<Publicacion> getPublicacionesActivasByUsuario(Long idUsuario) {
        return publicacionRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, "Activo");
    }


    /**
     * Buscar publicaciones por locación
     * @param locacion Texto a buscar en la locación
     * @return Lista de publicaciones que coinciden
     */
    public List<Publicacion> buscarPorLocacion(String locacion) {
        if (locacion == null || locacion.trim().isEmpty()) {
            throw new RuntimeException("La locación no puede estar vacía");
        }
        return publicacionRepository.findByLocacionContainingIgnoreCase(locacion);
    }


    // ========== MÉTODOS DE CREACIÓN (CREATE) ==========

    /**
     * Crear una nueva publicación
     * @param publicacion Datos de la publicación
     * @return Publicación creada
     */
    @Transactional
    public Publicacion createPublicacion(Publicacion publicacion) {
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(publicacion.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que la categoría existe
        Categoria categoria = categoriaRepository.findById(publicacion.getCategoria().getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Validar campos obligatorios
        if (publicacion.getTitulo() == null || publicacion.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("El título es obligatorio");
        }

        if (publicacion.getContenido() == null || publicacion.getContenido().trim().isEmpty()) {
            throw new RuntimeException("El contenido es obligatorio");
        }

        // Asignar valores por defecto
        publicacion.setUsuario(usuario);
        publicacion.setCategoria(categoria);
        publicacion.setEstado("Activo");  // Por defecto activo
        publicacion.setFechaPublicacion(LocalDateTime.now());

        return publicacionRepository.save(publicacion);
    }


    // ========== MÉTODOS DE ACTUALIZACIÓN (UPDATE) ==========

    /**
     * Actualizar una publicación existente
     * @param id ID de la publicación a actualizar
     * @param publicacionActualizada Datos actualizados
     * @return Publicación actualizada
     */
    @Transactional
    public Publicacion updatePublicacion(Long id, Publicacion publicacionActualizada) {
        // Buscar la publicación existente
        Publicacion publicacion = getPublicacionById(id);

        // Actualizar campos
        if (publicacionActualizada.getTitulo() != null && !publicacionActualizada.getTitulo().trim().isEmpty()) {
            publicacion.setTitulo(publicacionActualizada.getTitulo());
        }

        if (publicacionActualizada.getContenido() != null && !publicacionActualizada.getContenido().trim().isEmpty()) {
            publicacion.setContenido(publicacionActualizada.getContenido());
        }

        if (publicacionActualizada.getLocacion() != null) {
            publicacion.setLocacion(publicacionActualizada.getLocacion());
        }

        if (publicacionActualizada.getImagenes() != null) {
            publicacion.setImagenes(publicacionActualizada.getImagenes());
        }

        // Actualizar categoría si se proporcionó
        if (publicacionActualizada.getCategoria() != null &&
                publicacionActualizada.getCategoria().getIdCategoria() != null) {

            Categoria categoria = categoriaRepository.findById(publicacionActualizada.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            publicacion.setCategoria(categoria);
        }

        return publicacionRepository.save(publicacion);
    }


    /**
     * Desactivar una publicación (ocultar, pero no eliminar)
     * @param id ID de la publicación
     * @return Publicación desactivada
     */
    @Transactional
    public Publicacion desactivarPublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id);
        publicacion.setEstado("Inactivo");
        return publicacionRepository.save(publicacion);
    }


    /**
     * Activar una publicación previamente desactivada
     * @param id ID de la publicación
     * @return Publicación activada
     */
    @Transactional
    public Publicacion activarPublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id);
        publicacion.setEstado("Activo");
        return publicacionRepository.save(publicacion);
    }


    // ========== MÉTODOS DE ELIMINACIÓN (DELETE) ==========

    /**
     * Eliminar una publicación permanentemente de la base de datos
     * @param id ID de la publicación
     */
    @Transactional
    public void deletePublicacion(Long id) {
        Publicacion publicacion = getPublicacionById(id);
        publicacionRepository.delete(publicacion);
    }


    // ========== MÉTODOS DE VERIFICACIÓN ==========

    /**
     * Verificar si una publicación existe
     * @param id ID de la publicación
     * @return true si existe, false si no
     */
    public boolean existePublicacion(Long id) {
        return publicacionRepository.existsById(id);
    }


    /**
     * Verificar si un usuario es el propietario de una publicación
     * @param idPublicacion ID de la publicación
     * @param idUsuario ID del usuario
     * @return true si es propietario, false si no
     */
    public boolean esPropietario(Long idPublicacion, Long idUsuario) {
        Publicacion publicacion = getPublicacionById(idPublicacion);
        return publicacion.getUsuario().getIdUsuario().equals(idUsuario);
    }


    // ========== MÉTODOS ESTADÍSTICOS ==========

    /**
     * Contar total de publicaciones
     * @return Número total de publicaciones
     */
    public long contarTotalPublicaciones() {
        return publicacionRepository.count();
    }


    /**
     * Contar publicaciones activas
     * @return Número de publicaciones activas
     */
    public long contarPublicacionesActivas() {
        return publicacionRepository.findByEstado("Activo").size();
    }


    /**
     * Contar publicaciones de un usuario
     * @param idUsuario ID del usuario
     * @return Número de publicaciones del usuario
     */
    public long contarPublicacionesPorUsuario(Long idUsuario) {
        return publicacionRepository.findByUsuarioIdUsuario(idUsuario).size();
    }


    /**
     * Contar publicaciones por categoría
     * @param idCategoria ID de la categoría
     * @return Número de publicaciones en esa categoría
     */
    public long contarPublicacionesPorCategoria(Long idCategoria) {
        return publicacionRepository.findByCategoriaIdCategoria(idCategoria).size();
    }
}
