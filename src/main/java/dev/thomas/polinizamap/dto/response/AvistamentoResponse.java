package dev.thomas.polinizamap.dto.response;

import dev.thomas.polinizamap.entity.Avistamento;
import dev.thomas.polinizamap.enums.StatusAvistamento;

import java.time.LocalDateTime;

public record AvistamentoResponse(
        Long id,
        String usuarioNome,
        String especieNomePopular,
        String regiaoNome,
        String regiaoCidade,
        Double latitude,
        Double longitude,
        String descricao,
        LocalDateTime dataHora,
        StatusAvistamento status,
        String notaValidacao
) {
    public static AvistamentoResponse from(Avistamento a) {
        return new AvistamentoResponse(
                a.getId(),
                a.getUsuario().getNome(),
                a.getEspecie().getNomePopular(),
                a.getRegiao().getNome(),
                a.getRegiao().getCidade(),
                a.getLatitude(),
                a.getLongitude(),
                a.getDescricao(),
                a.getDataHora(),
                a.getStatus(),
                a.getNotaValidacao()
        );
    }
}