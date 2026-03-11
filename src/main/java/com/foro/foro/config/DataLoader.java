package com.foro.foro.config;

import com.foro.foro.modelo.Topico;
import com.foro.foro.seguridad.Usuario;
import com.foro.foro.seguridad.UsuarioRepository;
import com.foro.foro.repositorio.TopicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository,
                                      TopicoRepository topicoRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario(null, "Administrador", "admin@foro.com", passwordEncoder.encode("admin123"));
                usuarioRepository.save(admin);
            }
            if (topicoRepository.count() == 0) {
                // creamos algunos tópicos de ejemplo
                topicoRepository.save(new Topico("Bienvenida", "Bienvenidos al foro!", "General", "admin@foro.com"));
                topicoRepository.save(new Topico("Duda sobre Spring", "¿Cómo configuro seguridad?", "SpringBoot", "admin@foro.com"));
                topicoRepository.save(new Topico("Pregunta de Java","¿Qué es un record?","Java", "admin@foro.com"));
            }
        };
    }
}