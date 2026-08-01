package com.ars.adopet.dtos;

import com.ars.adopet.enums.TipoAcaoAdministrativa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcaoAdministrativaRequestDTO {

    @NotNull
    private String denunciaId;

    @NotNull
    private String usuarioAlvoId;

    @NotNull
    private String administradorId;

    @NotNull
    private TipoAcaoAdministrativa tipo;

    @NotBlank
    private String descricao;
}
