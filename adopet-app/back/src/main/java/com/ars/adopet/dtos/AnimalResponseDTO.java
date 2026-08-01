package com.ars.adopet.dtos;

import com.ars.adopet.enums.AdoptionStatus;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class AnimalResponseDTO {

    private String id;
    private String nome;
    private String especie;
    private String raca;
    private int idade;
    private boolean vacinado;
    private boolean castrado;
    private AdoptionStatus status;

    private Instant criadoEm;
    private Instant atualizadoEm;

    private UsuarioResumoDTO doador;

    private List<String> fotosUrls;
}
