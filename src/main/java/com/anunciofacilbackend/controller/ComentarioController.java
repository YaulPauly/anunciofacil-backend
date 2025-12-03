package com.anunciofacilbackend.controller;

import com.anunciofacilbackend.model.Comentario;
import com.anunciofacilbackend.servicio.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    /**
     * GET /api/comentarios
     * Obtener todos los comentarios
     */
    @GetMapping
    public ResponseEntity<List<Comentario>> getAllComentarios() {
        List<Comentario> comentarios = comentarioService.getAllComentarios();
        return ResponseEntity.ok(comentarios);
    }

    /**
     * GET /api/comentarios/{id}
     * Obtener comentario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comentario> getComentarioById(@PathVariable Long id) {
        try {
            Comentario comentario = comentarioService.getComentarioById(id);
            return ResponseEntity.ok(comentario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/comentarios/publicacion/{idPublicacion}
     * Obtener comentarios de una publicación específica
     */
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<Comentario>> getComentariosByPublicacion(@PathVariable Long idPublicacion) {
        List<Comentario> comentarios = comentarioService.getComentariosByPublicacion(idPublicacion);
        return ResponseEntity.ok(comentarios);
    }

    /**
     * GET /api/comentarios/usuario/{idUsuario}
     * Obtener comentarios de un usuario
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Comentario>> getComentariosByUsuario(@PathVariable Long idUsuario) {
        List<Comentario> comentarios = comentarioService.getComentariosByUsuario(idUsuario);
        return ResponseEntity.ok(comentarios);
    }

    /**
     * POST /api/comentarios
     * Crear un nuevo comentario (requiere login)
     */
    @PostMapping
    public ResponseEntity<Comentario> createComentario(@Valid @RequestBody Comentario comentario) {
        try {
            Comentario nuevoComentario = comentarioService.createComentario(comentario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/comentarios/{id}
     * Actualizar un comentario (solo propietario)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comentario> updateComentario(
            @PathVariable Long id,
            @Valid @RequestBody Comentario comentario) {
        try {
            Comentario comentarioActualizado = comentarioService.updateComentario(id, comentario);
            return ResponseEntity.ok(comentarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/comentarios/{id}
     * Eliminar un comentario (propietario o admin)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        try {
            comentarioService.deleteComentario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
