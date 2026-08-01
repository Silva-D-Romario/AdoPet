package com.ars.adopet.dtos;

import com.ars.adopet.enums.UserRole;
import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private String id;
    private String nomeCompleto;
    private String email;
    private String telefone;
    private boolean verificado;
    private UserRole papel;
    private EnderecoDTO endereco;
}
