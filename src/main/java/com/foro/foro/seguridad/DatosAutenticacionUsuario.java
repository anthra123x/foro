package com.foro.foro.seguridad;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}