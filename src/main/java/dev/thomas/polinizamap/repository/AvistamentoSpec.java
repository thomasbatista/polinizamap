package dev.thomas.polinizamap.repository;

import dev.thomas.polinizamap.entity.Avistamento;
import dev.thomas.polinizamap.enums.StatusAvistamento;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AvistamentoSpec {

    private AvistamentoSpec() {}

    public static Specification<Avistamento> porEspecie(Long especieId) {
        return (root, query, cb) -> especieId == null ? null
                : cb.equal(root.get("especie").get("id"), especieId);
    }

    public static Specification<Avistamento> porRegiao(Long regiaoId) {
        return (root, query, cb) -> regiaoId == null ? null
                : cb.equal(root.get("regiao").get("id"), regiaoId);
    }

    public static Specification<Avistamento> porStatus(StatusAvistamento status) {
        return (root, query, cb) -> status == null ? null
                : cb.equal(root.get("status"), status);
    }

    public static Specification<Avistamento> porPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return (root, query, cb) -> {
            if (inicio != null && fim != null)
                return cb.between(root.get("dataHora"), inicio, fim);
            if (inicio != null)
                return cb.greaterThanOrEqualTo(root.get("dataHora"), inicio);
            if (fim != null)
                return cb.lessThanOrEqualTo(root.get("dataHora"), fim);
            return null;
        };
    }

    public static Specification<Avistamento> porUsuario(Long usuarioId) {
        return (root, query, cb) -> usuarioId == null ? null
                : cb.equal(root.get("usuario").get("id"), usuarioId);
    }
}