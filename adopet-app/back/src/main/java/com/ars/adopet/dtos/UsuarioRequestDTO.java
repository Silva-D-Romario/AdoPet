package com.ars.adopet.dtos;

import com.ars.adopet.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank
    private String nomeCompleto;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String telefone;

    private UserRole papel = UserRole.USUARIO;

    private EnderecoDTO endereco;
}
