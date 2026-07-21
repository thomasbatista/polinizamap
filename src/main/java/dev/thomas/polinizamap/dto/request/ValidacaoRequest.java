package dev.thomas.polinizamap.dto.request;

import dev.thomas.polinizamap.enums.StatusAvistamento;
import jakarta.validation.constraints.NotNull;

public record ValidacaoRequest(

        @NotNull(message = "Status é obrigatório")
        StatusAvistamento status,

        String notaValidacao
) {}