package com.ars.adopet.dtos;

import lombok.Data;

@Data
public class DenunciaRequestDTO {

    private String usuarioId;
    private String animalId;
    private String usuarioDenunciadoId;

    private String categoria;
    private String descricao;
}
