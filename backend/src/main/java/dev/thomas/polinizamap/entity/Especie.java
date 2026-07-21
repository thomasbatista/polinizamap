package dev.thomas.polinizamap.entity;

import dev.thomas.polinizamap.enums.StatusConservacao;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_popular", nullable = false)
    private String nomePopular;

    @Column(name = "nome_cientifico", nullable = false, unique = true)
    private String nomeCientifico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_conservacao", nullable = false)
    private StatusConservacao statusConservacao;
}
