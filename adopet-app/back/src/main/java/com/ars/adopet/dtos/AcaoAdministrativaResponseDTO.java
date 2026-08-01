package com.ars.adopet.dtos;

import lombok.Data;
import java.time.Instant;

@Data
public class AcaoAdministrativaResponseDTO {

    private String id;

    private UsuarioResumoDTO usuarioAlvo;
    private UsuarioResumoDTO administrador;

    private String tipo;
    private String descricao;

    private Instant criadoEm;
}
