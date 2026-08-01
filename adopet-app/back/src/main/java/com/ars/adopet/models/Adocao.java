package com.ars.adopet.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    private SolicitacaoAdocao solicitacao;

    private Instant dataAdocao = Instant.now();
}
