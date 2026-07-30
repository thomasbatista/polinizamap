package dev.thomas.polinizamap;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegiaoControllerTest extends BaseIntegrationTest {

    @Test
    void adminDeveCriarRegiao() throws Exception {
        mockMvc.perform(post("/regioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content("""
                                {
                                    "nome": "Parque Estadual",
                                    "cidade": "São Paulo",
                                    "estado": "SP"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Parque Estadual"));
    }

    @Test
    void cidadaoNaoDeveCriarRegiao() throws Exception {
        mockMvc.perform(post("/regioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenCidadao)
                        .content("""
                                {
                                    "nome": "Parque Estadual",
                                    "cidade": "São Paulo",
                                    "estado": "SP"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornar409AoCriarRegiaoDuplicadaNaMesmaCidade() throws Exception {
        String body = """
                {
                    "nome": "Parque Estadual",
                    "cidade": "São Paulo",
                    "estado": "SP"
                }
                """;

        mockMvc.perform(post("/regioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/regioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void deveListarRegioesAutenticado() throws Exception {
        mockMvc.perform(get("/regioes")
                        .header("Authorization", "Bearer " + tokenCidadao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(get("/regioes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar404AoBuscarRegiaoInexistente() throws Exception {
        mockMvc.perform(get("/regioes/99999")
                        .header("Authorization", "Bearer " + tokenCidadao))
                .andExpect(status().isNotFound());
    }

    @Test
    void adminDeveDeletarRegiao() throws Exception {
        String response = mockMvc.perform(post("/regioes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content("""
                                {
                                    "nome": "Jardim Botânico",
                                    "cidade": "Curitiba",
                                    "estado": "PR"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/regioes/" + id)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/regioes/" + id)
                        .header("Authorization", "Bearer " + tokenCidadao))
                .andExpect(status().isNotFound());
    }
}
