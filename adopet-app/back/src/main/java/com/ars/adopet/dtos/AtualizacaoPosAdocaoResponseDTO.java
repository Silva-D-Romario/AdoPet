package com.ars.adopet.dtos;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class AtualizacaoPosAdocaoResponseDTO {

    private String id;
    private String descricao;
    private Instant criadoEm;

    private List<String> fotos;
}
