package com.ars.adopet.dtos;

import lombok.Data;

@Data
public class AtualizarSenhaDTO {
    private String senhaAtual;
    private String novaSenha;
}