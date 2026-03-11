package com.foro.foro.dto;

import com.foro.foro.seguridad.Usuario;

public record DatosRespuestaUsuario(Long id, String nombre, String email) {
    public DatosRespuestaUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }
}