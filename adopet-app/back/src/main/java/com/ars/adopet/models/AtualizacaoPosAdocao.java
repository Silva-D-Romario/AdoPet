package com.ars.adopet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoPosAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "adocao_id", nullable = false)
    private Adocao adocao;

    @NotBlank
    private String descricao;

    private Instant criadoEm = Instant.now();

    @OneToMany(mappedBy = "atualizacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoAtualizacao> fotos = new ArrayList<>();
}