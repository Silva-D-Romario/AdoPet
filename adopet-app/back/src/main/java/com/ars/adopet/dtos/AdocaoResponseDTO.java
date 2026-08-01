package com.ars.adopet.dtos;

import lombok.Data;
import java.time.Instant;

@Data
public class AdocaoResponseDTO {
    private String id;
    private SolicitacaoAdocaoResponseDTO solicitacao;
    private Instant dataAdocao;
}

