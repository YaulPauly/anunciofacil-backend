package com.anunciofacilbackend.servicio;

import com.anunciofacilbackend.model.Comentario;
import com.anunciofacilbackend.model.Usuario;
import com.anunciofacilbackend.model.Publicacion;
import com.anunciofacilbackend.repository.ComentarioRepository;
import com.anunciofacilbackend.repository.UsuarioRepository;
import com.anunciofacilbackend.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PublicacionRepository publicacionRepository;

    public List<Comentario> getAllComentarios() {
        return comentarioRepository.findAll();
    }

    public Comentario getComentarioById(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + id));
    }

    public List<Comentario> getComentariosByPublicacion(Long idPublicacion) {
        return comentarioRepository.findByPublicacionIdPublicacion(idPublicacion);
    }

    public List<Comentario> getComentariosByUsuario(Long idUsuario) {
        return comentarioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    @Transactional
    public Comentario createComentario(Comentario comentario) {
        Usuario usuario = usuarioRepository.findById(comentario.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Publicacion publicacion = publicacionRepository.findById(comentario.getPublicacion().getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        comentario.setUsuario(usuario);
        comentario.setPublicacion(publicacion);
        comentario.setFechaPublicacion(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    @Transactional
    public Comentario updateComentario(Long id, Comentario comentarioActualizado) {
        Comentario comentario = getComentarioById(id);
        comentario.setContenido(comentarioActualizado.getContenido());
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void deleteComentario(Long id) {
        Comentario comentario = getComentarioById(id);
        comentarioRepository.delete(comentario);
    }

    public boolean esPropietario(Long idComentario, Long idUsuario) {
        Comentario comentario = getComentarioById(idComentario);
        return comentario.getUsuario().getIdUsuario().equals(idUsuario);
    }
}