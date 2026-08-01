package com.ars.adopet.models;

import com.ars.adopet.enums.AdoptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String nome;

    @NotBlank
    private String especie;

    private String raca;

    private int idade;

    private boolean vacinado;

    private boolean castrado;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status = AdoptionStatus.DISPONIVEL;

    private Instant criadoEm = Instant.now();
    private Instant atualizadoEm;

    // DONO / DOADOR
    @ManyToOne
    @JoinColumn(name = "doador_id")
    private Usuario doador;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoAnimal> fotos = new ArrayList<>();
}
