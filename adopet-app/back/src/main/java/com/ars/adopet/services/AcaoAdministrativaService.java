package com.ars.adopet.services;

import com.ars.adopet.dtos.AcaoAdministrativaRequestDTO;
import com.ars.adopet.dtos.AcaoAdministrativaResponseDTO;
import com.ars.adopet.dtos.UsuarioResumoDTO;
import com.ars.adopet.enums.StatusDenuncia;
import com.ars.adopet.enums.UserRole;
import com.ars.adopet.models.AcaoAdministrativa;
import com.ars.adopet.models.Denuncia;
import com.ars.adopet.models.Usuario;
import com.ars.adopet.repositories.AcaoAdministrativaRepository;
import com.ars.adopet.repositories.DenunciaRepository;
import com.ars.adopet.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AcaoAdministrativaService {

    private final AcaoAdministrativaRepository acaoRepository;
    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;

    public AcaoAdministrativaService(
            AcaoAdministrativaRepository acaoRepository,
            DenunciaRepository denunciaRepository,
            UsuarioRepository usuarioRepository) {
        this.acaoRepository = acaoRepository;
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AcaoAdministrativaResponseDTO aplicar(AcaoAdministrativaRequestDTO dto) {

        Denuncia denuncia = denunciaRepository.findById(dto.getDenunciaId())
                .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

        Usuario admin = usuarioRepository.findById(dto.getAdministradorId())
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));

        if (admin.getPapel() != UserRole.USUARIO.ADMINISTRADOR) {
            throw new RuntimeException("Usuário não possui permissão administrativa");
        }

        Usuario alvo = usuarioRepository.findById(dto.getUsuarioAlvoId())
                .orElseThrow(() -> new RuntimeException("Usuário alvo não encontrado"));

        AcaoAdministrativa acao = new AcaoAdministrativa();
        acao.setAdministrador(admin);
        acao.setUsuarioAlvo(alvo);
        acao.setTipo(dto.getTipo().name());
        acao.setDescricao(dto.getDescricao());
        acao.setCriadoEm(Instant.now());

        AcaoAdministrativa salva = acaoRepository.save(acao);

        denuncia.setStatus(StatusDenuncia.RESOLVIDA);
        denuncia.setAtualizadoEm(Instant.now());
        denunciaRepository.save(denuncia);

        return toResponseDTO(salva);
    }


    private AcaoAdministrativaResponseDTO toResponseDTO(AcaoAdministrativa acao) {

        AcaoAdministrativaResponseDTO dto = new AcaoAdministrativaResponseDTO();
        dto.setId(acao.getId());
        dto.setTipo(acao.getTipo());
        dto.setDescricao(acao.getDescricao());
        dto.setCriadoEm(acao.getCriadoEm());

        UsuarioResumoDTO admin = new UsuarioResumoDTO();
        admin.setId(acao.getAdministrador().getId());
        admin.setNomeCompleto(acao.getAdministrador().getNomeCompleto());
        dto.setAdministrador(admin);

        UsuarioResumoDTO alvo = new UsuarioResumoDTO();
        alvo.setId(acao.getUsuarioAlvo().getId());
        alvo.setNomeCompleto(acao.getUsuarioAlvo().getNomeCompleto());
        dto.setUsuarioAlvo(alvo);

        return dto;
    }

}
