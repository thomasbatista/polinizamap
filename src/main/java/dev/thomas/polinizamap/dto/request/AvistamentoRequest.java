package dev.thomas.polinizamap.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDateTime;

public record AvistamentoRequest(

        @NotNull(message = "Espécie é obrigatória")
        Long especieId,

        @NotNull(message = "Região é obrigatória")
        Long regiaoId,

        @NotNull(message = "Latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude inválida")
        @DecimalMax(value = "90.0", message = "Latitude inválida")
        Double latitude,

        @NotNull(message = "Longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude inválida")
        @DecimalMax(value = "180.0", message = "Longitude inválida")
        Double longitude,

        String descricao,

        @NotNull(message = "Data e hora são obrigatórias")
        @PastOrPresent(message = "Data não pode ser no futuro")
        LocalDateTime dataHora
) {}