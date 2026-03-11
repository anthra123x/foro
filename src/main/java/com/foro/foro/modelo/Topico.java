package com.foro.foro.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private boolean status = true;
    private String autor;
    private String curso;

    public Topico(String titulo, String mensaje, String curso, String autor) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.curso = curso;
        this.autor = autor;
    }

    /**
     * Actualiza los datos del tópico con la información enviada en el DTO.
     * Sólo cambia los valores no nulos para permitir actualizaciones parciales.
     */
    public void actualizarDatos(com.foro.foro.dto.DatosActualizarTopico datos) {
        if (datos.titulo() != null) this.titulo = datos.titulo();
        if (datos.mensaje() != null) this.mensaje = datos.mensaje();
        if (datos.curso() != null) this.curso = datos.curso();
    }

    /**
     * Marca el tópico como inactivo en lugar de borrarlo físicamente.
     */
    public void desactivar() {
        this.status = false;
    }
}
