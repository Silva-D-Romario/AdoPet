package com.ars.adopet.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AtualizacaoPosAdocaoRequestDTO {

    private String adocaoId;
    private String descricao;

    private List<String> fotos;
}
