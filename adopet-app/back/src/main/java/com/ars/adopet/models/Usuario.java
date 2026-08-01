package com.ars.adopet.models;

import com.ars.adopet.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String nomeCompleto;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senhaHash; // futuro: renomear para "password" com Spring Security

    private String telefone;

    private boolean verificado = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole papel = UserRole.USUARIO;

    @Embedded
    private Endereco endereco;

    private Instant criadoEm = Instant.now();
    private Instant atualizadoEm;

    @OneToMany(mappedBy = "doador")
    private List<Animal> animaisDoados = new ArrayList<>();

    @OneToMany(mappedBy = "solicitante")
    private List<SolicitacaoAdocao> solicitacoes = new ArrayList<>();


    @OneToMany(mappedBy = "usuario")
    private List<Denuncia> denuncias = new ArrayList<>();


    public boolean isAdmin() {
        return papel == UserRole.ADMINISTRADOR;
    }
}
