// src/test/java/dev/thomas/polinizamap/AvistamentoControllerTest.java
package dev.thomas.polinizamap;

import dev.thomas.polinizamap.entity.Especie;
import dev.thomas.polinizamap.entity.Regiao;
import dev.thomas.polinizamap.enums.StatusConservacao;
import dev.thomas.polinizamap.repository.EspecieRepository;
import dev.thomas.polinizamap.repository.RegiaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvistamentoControllerTest extends BaseIntegrationTest {

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private RegiaoRepository regiaoRepository;

    private Long especieId;
    private Long regiaoId;

    @BeforeEach
    void setupDados() {
        Especie especie = especieRepository.save(Especie.builder()
                .nomePopular("Abelha Jataí")
                .nomeCientifico("Tetragonisca angustula")
                .statusConservacao(StatusConservacao.POUCO_PREOCUPANTE)
                .build());
        especieId = especie.getId();

        Regiao regiao = regiaoRepository.save(Regiao.builder()
                .nome("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .build());
        regiaoId = regiao.getId();
    }

    @Test
    void cidadaoDeveRegistrarAvistamento() throws Exception {
        mockMvc.perform(post("/avistamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenCidadao)
                        .content("""
                                {
                                    "especieId": %d,
                                    "regiaoId": %d,
                                    "latitude": -23.5505,
                                    "longitude": -46.6333,
                                    "descricao": "Vista no jardim",
                                    "dataHora": "2026-06-01T10:00:00"
                                }
                                """.formatted(especieId, regiaoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void pesquisadorDeveValidarAvistamento() throws Exception {
        // Registra
        String response = mockMvc.perform(post("/avistamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenCidadao)
                        .content("""
                                {
                                    "especieId": %d,
                                    "regiaoId": %d,
                                    "latitude": -23.5505,
                                    "longitude": -46.6333,
                                    "dataHora": "2026-06-01T10:00:00"
                                }
                                """.formatted(especieId, regiaoId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Valida
        mockMvc.perform(patch("/avistamentos/" + id + "/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenPesquisador)
                        .content("""
                                {
                                    "status": "APROVADO",
                                    "notaValidacao": "Confirmado!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APROVADO"));
    }

    @Test
    void cidadaoNaoDeveValidarAvistamento() throws Exception {
        mockMvc.perform(patch("/avistamentos/1/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenCidadao)
                        .content("""
                                {
                                    "status": "APROVADO"
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}