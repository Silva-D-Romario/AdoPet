package com.ars.adopet.dtos;

import com.ars.adopet.enums.AdoptionStatus;
import lombok.Data;

import java.util.List;

@Data
public class AnimalRequestDTO {

    private String nome;
    private String especie;
    private String raca;
    private int idade;
    private boolean vacinado;
    private boolean castrado;
    private AdoptionStatus status;

    private String doadorId;

    private List<String> fotosUrls;
}

