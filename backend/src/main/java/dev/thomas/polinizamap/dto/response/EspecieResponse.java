package dev.thomas.polinizamap.dto.response;

import dev.thomas.polinizamap.entity.Especie;
import dev.thomas.polinizamap.enums.StatusConservacao;

public record EspecieResponse(
        Long id,
        String nomePopular,
        String nomeCientifico,
        StatusConservacao statusConservacao
) {
    public static EspecieResponse from(Especie especie) {
        return new EspecieResponse(
                especie.getId(),
                especie.getNomePopular(),
                especie.getNomeCientifico(),
                especie.getStatusConservacao()
        );
    }
}