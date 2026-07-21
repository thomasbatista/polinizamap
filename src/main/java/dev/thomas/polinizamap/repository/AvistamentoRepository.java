package dev.thomas.polinizamap.repository;

import dev.thomas.polinizamap.entity.Avistamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AvistamentoRepository extends JpaRepository<Avistamento, Long>, JpaSpecificationExecutor<Avistamento> {
}
