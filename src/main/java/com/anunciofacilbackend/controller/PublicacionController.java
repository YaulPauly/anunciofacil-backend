package com.anunciofacilbackend.controller;

import com.anunciofacilbackend.model.Publicacion;
import com.anunciofacilbackend.service.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }


    /**
     * Obtener todas las publicaciones
     */
    @GetMapping
    public ResponseEntity<List<Publicacion>> getAllPublicaciones() {
        List<Publicacion> publicaciones = publicacionService.getAllPublicaciones();
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * GET /api/publicaciones/{id}
     * Obtener publicación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> getPublicacionById(@PathVariable Long id) {
        Publicacion publicacion = publicacionService.getPublicacionById(id);
        return ResponseEntity.ok(publicacion);
    }

    /**
     * Obtener solo publicaciones activas (públicas)
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Publicacion>> getPublicacionesActivas() {
        List<Publicacion> publicaciones = publicacionService.getPublicacionesActivas();
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Obtener publicaciones por categoría
     */
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Publicacion>> getByCategoria(@PathVariable Long idCategoria) {
        List<Publicacion> publicaciones = publicacionService.getPublicacionesByCategoria(idCategoria);
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Obtener publicaciones activas por categoría
     */
    @GetMapping("/categoria/{idCategoria}/activas")
    public ResponseEntity<List<Publicacion>> getActivasByCategoria(@PathVariable Long idCategoria) {
        List<Publicacion> publicaciones = publicacionService.getPublicacionesActivasByCategoria(idCategoria);
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Obtener publicaciones de un usuario (mis anuncios)
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Publicacion>> getByUsuario(@PathVariable Long idUsuario) {
        List<Publicacion> publicaciones = publicacionService.getPublicacionesByUsuario(idUsuario);
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Buscar publicaciones por título
     */
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Publicacion>> buscarPorTitulo(@RequestParam String q) {
        List<Publicacion> publicaciones = publicacionService.buscarPorTitulo(q);
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Buscar publicaciones por locación
     */
    @GetMapping("/buscar/locacion")
    public ResponseEntity<List<Publicacion>> buscarPorLocacion(@RequestParam String q) {
        List<Publicacion> publicaciones = publicacionService.buscarPorLocacion(q);
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Crear una nueva publicación
     */
    @PostMapping
    public ResponseEntity<Publicacion> createPublicacion(@Valid @RequestBody Publicacion publicacion) {
        try {
            Publicacion nuevaPublicacion = publicacionService.createPublicacion(publicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPublicacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar una publicación existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> updatePublicacion(
            @PathVariable Long id,
            @Valid @RequestBody Publicacion publicacion) {
        try {
            Publicacion publicacionActualizada = publicacionService.updatePublicacion(id, publicacion);
            return ResponseEntity.ok(publicacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desactivar una publicación (ocultar)
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Publicacion> desactivarPublicacion(@PathVariable Long id) {
        try {
            Publicacion publicacion = publicacionService.desactivarPublicacion(id);
            return ResponseEntity.ok(publicacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Activar una publicación previamente desactivada
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<Publicacion> activarPublicacion(@PathVariable Long id) {
        try {
            Publicacion publicacion = publicacionService.activarPublicacion(id);
            return ResponseEntity.ok(publicacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Eliminar una publicación permanentemente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicacion(@PathVariable Long id) {
        try {
            publicacionService.deletePublicacion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Contar total de publicaciones
     */
    @GetMapping("/estadisticas/total")
    public ResponseEntity<Long> contarTotal() {
        long total = publicacionService.contarTotal();
        return ResponseEntity.ok(total);
    }

    /**
     * Contar publicaciones activas
     */
    @GetMapping("/estadisticas/activas")
    public ResponseEntity<Long> contarActivas() {
        long activas = publicacionService.contarActivas();
        return ResponseEntity.ok(activas);
    }
}