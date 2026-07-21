package dev.thomas.polinizamap;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EspecieControllerTest extends BaseIntegrationTest {

    @Test
    void adminDeveCriarEspecie() throws Exception {
        mockMvc.perform(post("/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content("""
                                {
                                    "nomePopular": "Abelha Jataí",
                                    "nomeCientifico": "Tetragonisca angustula",
                                    "statusConservacao": "POUCO_PREOCUPANTE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nomePopular").value("Abelha Jataí"));
    }

    @Test
    void cidadaoNaoDeveCriarEspecie() throws Exception {
        mockMvc.perform(post("/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenCidadao)
                        .content("""
                                {
                                    "nomePopular": "Abelha Jataí",
                                    "nomeCientifico": "Tetragonisca angustula",
                                    "statusConservacao": "POUCO_PREOCUPANTE"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornar409AoCriarEspecieDuplicada() throws Exception {
        String body = """
                {
                    "nomePopular": "Abelha Jataí",
                    "nomeCientifico": "Tetragonisca angustula",
                    "statusConservacao": "POUCO_PREOCUPANTE"
                }
                """;

        mockMvc.perform(post("/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void deveListarEspeciesAutenticado() throws Exception {
        mockMvc.perform(get("/especies")
                        .header("Authorization", "Bearer " + tokenCidadao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(get("/especies"))
                .andExpect(status().isUnauthorized());
    }
}