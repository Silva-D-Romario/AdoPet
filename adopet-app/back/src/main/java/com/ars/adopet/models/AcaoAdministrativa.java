package com.ars.adopet.models;

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
public class AcaoAdministrativa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario_alvo_id", nullable = false)
    private Usuario usuarioAlvo;

    @ManyToOne
    @JoinColumn(name = "administrador_id", nullable = false)
    private Usuario administrador;

    @NotBlank
    private String tipo; // BLOQUEIO, ADVERTENCIA, etc.

    @NotBlank
    private String descricao;

    private Instant criadoEm = Instant.now();
}