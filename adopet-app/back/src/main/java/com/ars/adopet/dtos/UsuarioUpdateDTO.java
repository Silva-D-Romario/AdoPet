package com.ars.adopet.dtos;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    private String nomeCompleto;
    private String telefone;
    private EnderecoDTO endereco;
}
