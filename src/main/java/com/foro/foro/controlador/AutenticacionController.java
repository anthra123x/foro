package com.foro.foro.controlador;

import com.foro.foro.seguridad.DatosAutenticacionUsuario;
import com.foro.foro.seguridad.DatosTokenJWT;
import com.foro.foro.seguridad.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<DatosTokenJWT> autenticar(@RequestBody @Valid DatosAutenticacionUsuario datos) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(datos.email(), datos.password())
        );
        String token = jwtUtil.generarToken((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new DatosTokenJWT(token));
    }
}