package com.ars.adopet.dtos;

import com.ars.adopet.enums.StatusDenuncia;
import lombok.Data;
import java.time.Instant;

@Data
public class DenunciaResponseDTO {

    private String id;
    private UsuarioResumoDTO usuario;
    private AnimalResumoDTO animal;
    private UsuarioResumoDTO usuarioDenunciado;
    private String categoria;
    private String descricao;

    private StatusDenuncia status;

    private Instant criadoEm;
    private Instant atualizadoEm;
}
