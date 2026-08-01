package com.ars.adopet.models;

import com.ars.adopet.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Usuario solicitante;

    @ManyToOne
    private Animal animal;

    private Instant criadoEm = Instant.now();
    private Instant atualizadoEm;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDENTE;
}
