package dev.thomas.polinizamap.dto.response;

import dev.thomas.polinizamap.entity.Regiao;

public record RegiaoResponse(
        Long id,
        String nome,
        String cidade,
        String estado
) {
    public static RegiaoResponse from(Regiao regiao) {
        return new RegiaoResponse(
                regiao.getId(),
                regiao.getNome(),
                regiao.getCidade(),
                regiao.getEstado()
        );
    }
}