package com.ars.adopet.dtos;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class TransporteResponseDTO {

    private String id;
    private AdocaoResponseDTO adocao;
    private Instant criadoEm;
    private List<RastreamentoTransporteDTO> rastreamentos;
}
