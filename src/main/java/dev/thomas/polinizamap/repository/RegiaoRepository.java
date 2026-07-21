package dev.thomas.polinizamap.repository;

import dev.thomas.polinizamap.entity.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegiaoRepository extends JpaRepository<Regiao, Long> {

    boolean existsByNomeAndCidade(String nome, String cidade);
}
