package com.ars.adopet.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    private Adocao adocao;

    private Instant criadoEm = Instant.now();

    @OneToMany(mappedBy = "transporte")
    private List<RastreamentoTransporte> rastreamentos = new ArrayList<>();
}
