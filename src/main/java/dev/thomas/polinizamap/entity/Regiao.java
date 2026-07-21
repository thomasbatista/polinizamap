package dev.thomas.polinizamap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regioes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false, columnDefinition = "VARCHAR(2)")
    private String estado;
}
