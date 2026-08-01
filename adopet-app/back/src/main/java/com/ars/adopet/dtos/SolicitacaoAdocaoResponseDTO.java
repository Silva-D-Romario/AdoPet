package com.ars.adopet.dtos;

import com.ars.adopet.enums.RequestStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class SolicitacaoAdocaoResponseDTO {

    private String id;
    private UsuarioResumoDTO solicitante;
    private AnimalResumoDTO animal;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private RequestStatus status;
}
