package dev.thomas.polinizamap.dto.request;

import dev.thomas.polinizamap.enums.StatusConservacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EspecieRequest(

        @NotBlank(message = "Nome popular é obrigatório")
        String nomePopular,

        @NotBlank(message = "Nome científico é obrigatório")
        String nomeCientifico,

        @NotNull(message = "Status de conservação é obrigatório")
        StatusConservacao statusConservacao
) {}