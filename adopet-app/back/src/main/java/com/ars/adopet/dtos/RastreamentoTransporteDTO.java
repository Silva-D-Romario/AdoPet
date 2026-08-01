package com.ars.adopet.dtos;

import com.ars.adopet.enums.ReportStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class RastreamentoTransporteDTO {

    private String id;
    private ReportStatus status;
    private String mensagem;
    private Instant criadoEm;
}
