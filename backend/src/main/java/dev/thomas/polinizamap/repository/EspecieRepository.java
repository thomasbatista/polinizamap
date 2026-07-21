package dev.thomas.polinizamap.repository;

import dev.thomas.polinizamap.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieRepository extends JpaRepository<Especie, Long> {

    boolean existsByNomeCientifico(String nomeCientifico);
}
