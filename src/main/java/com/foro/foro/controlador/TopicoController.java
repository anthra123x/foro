package com.foro.foro.controlador;

import com.foro.foro.dto.DatosRegistroTopico;
import com.foro.foro.dto.DatosRespuestaTopico;
import com.foro.foro.dto.DatosActualizarTopico;
import com.foro.foro.dto.DatosListadoTopico;
import com.foro.foro.modelo.Topico;
import com.foro.foro.repositorio.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    /**
     * Crea un tópico nuevo. El autor se toma del usuario que está autenticado en el contexto.
     */
    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder) {
        // el nombre de usuario (email) viene en el principal de Spring Security
        String autor = SecurityContextHolder.getContext().getAuthentication().getName();
        Topico topico = topicoRepository.save(new Topico(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje(), datosRegistroTopico.curso(), autor));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    /**
     * Lista todos los tópicos activos con paginación.
     */
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(Pageable paginacion) {
        Page<Topico> page = topicoRepository.findAllByStatusTrue(paginacion);
        Page<DatosListadoTopico> dtoPage = page.map(DatosListadoTopico::new);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Devuelve los detalles de un único tópico.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detalleTopico(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .map(DatosRespuestaTopico::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza un tópico existente. Solo campos no nulos son modificados.
     */
    @PutMapping
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@RequestBody @Valid DatosActualizarTopico datos) {
        Topico topico = topicoRepository.getReferenceById(datos.id());
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!usuario.equals(topico.getAutor())) {
            return ResponseEntity.status(403).build();
        }
        topico.actualizarDatos(datos);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    /**
     * "Elimina" un tópico marcándolo como inactivo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!usuario.equals(topico.getAutor())) {
            return ResponseEntity.status(403).build();
        }
        topico.desactivar();
        return ResponseEntity.ok().build();
    }
}
