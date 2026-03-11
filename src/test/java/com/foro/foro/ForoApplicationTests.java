package com.foro.foro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ForoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    /**
     * Prueba de flujo básico: registrar usuario, autenticarse, crear tópico y listar.
     */
    @Test
    void flujoBasicoUsuarioYTopico() throws Exception {
        // registrar un usuario
        String userJson = "{\"nombre\":\"Juan\",\"email\":\"juan@example.com\",\"password\":\"123456\"}";
        mockMvc.perform(post("/usuarios")
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isCreated());

        // autenticarse
        String authJson = "{\"email\":\"juan@example.com\",\"password\":\"123456\"}";
        MvcResult authResult = mockMvc.perform(post("/auth")
                .contentType("application/json")
                .content(authJson))
                .andExpect(status().isOk())
                .andReturn();
        String token = new ObjectMapper()
                .readTree(authResult.getResponse().getContentAsString())
                .get("token").asText();

        // crear un tópico
        String topicoJson = "{\"titulo\":\"Duda Java\",\"mensaje\":\"¿Cómo hago X?\",\"curso\":\"Java 101\"}";
        mockMvc.perform(post("/topicos")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(topicoJson))
                .andExpect(status().isCreated());

        // listar tópicos y comprobar que aparece el creado
        MvcResult listResult = mockMvc.perform(get("/topicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Duda Java"))
                .andReturn();

        // obtener id del tópico recién creado
        Long topicoId = new ObjectMapper()
                .readTree(listResult.getResponse().getContentAsString())
                .get("content").get(0).get("id").asLong();

        // intentar actualizar con otro usuario (debe fallar)
        String otherUserJson = "{\"nombre\":\"Ana\",\"email\":\"ana@example.com\",\"password\":\"abcdef\"}";
        mockMvc.perform(post("/usuarios")
                .contentType("application/json")
                .content(otherUserJson))
                .andExpect(status().isCreated());
        String otherAuthJson = "{\"email\":\"ana@example.com\",\"password\":\"abcdef\"}";
        MvcResult otherAuth = mockMvc.perform(post("/auth")
                .contentType("application/json")
                .content(otherAuthJson))
                .andExpect(status().isOk())
                .andReturn();
        String otherToken = new ObjectMapper()
                .readTree(otherAuth.getResponse().getContentAsString())
                .get("token").asText();

        String updateJson = "{\"id\":" + topicoId + ",\"titulo\":\"Titulo mod\"}";
        mockMvc.perform(put("/topicos")
                .header("Authorization", "Bearer " + otherToken)
                .contentType("application/json")
                .content(updateJson))
                .andExpect(status().isForbidden());

        // propietario actualiza correctamente
        mockMvc.perform(put("/topicos")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(updateJson))
                .andExpect(status().isOk());

        // propietario elimina
        mockMvc.perform(delete("/topicos/" + topicoId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // después de eliminar ya no aparece en la lista
        mockMvc.perform(get("/topicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

}
