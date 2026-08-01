package com.ars.adopet.models;

import com.ars.adopet.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RastreamentoTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Transporte transporte;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String mensagem;

    private Instant criadoEm = Instant.now();
}
