package com.ars.adopet.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoAtualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "atualizacao_id")
    private AtualizacaoPosAdocao atualizacao;
}