package com.ars.adopet.models;

import com.ars.adopet.enums.StatusDenuncia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // denunciante

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal; // opcional

    @ManyToOne
    @JoinColumn(name = "usuario_denunciado_id")
    private Usuario usuarioDenunciado; // opcional

    @NotBlank
    private String categoria;

    @NotBlank
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusDenuncia status = StatusDenuncia.PENDENTE;

    private Instant criadoEm = Instant.now();
    private Instant atualizadoEm;
}