package com.ars.adopet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;
}
