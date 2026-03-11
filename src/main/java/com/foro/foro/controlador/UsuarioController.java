package com.foro.foro.controlador;

import com.foro.foro.dto.DatosRegistroUsuario;
import com.foro.foro.dto.DatosRespuestaUsuario;
import com.foro.foro.seguridad.Usuario;
import com.foro.foro.seguridad.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datos,
                                                                   UriComponentsBuilder uriBuilder) {
        Usuario usuario = new Usuario(null, datos.nombre(), datos.email(), passwordEncoder.encode(datos.password()));
        usuarioRepository.save(usuario);
        DatosRespuestaUsuario respuesta = new DatosRespuestaUsuario(usuario);
        URI url = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(respuesta);
    }
}