package com.ars.adopet.services;

import com.ars.adopet.dtos.DenunciaRequestDTO;
import com.ars.adopet.dtos.DenunciaResponseDTO;
import com.ars.adopet.dtos.UsuarioResumoDTO;
import com.ars.adopet.dtos.AnimalResumoDTO;
import com.ars.adopet.enums.StatusDenuncia;
import com.ars.adopet.models.*;
import com.ars.adopet.repositories.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnimalRepository animalRepository;

    public DenunciaService(DenunciaRepository denunciaRepository,
                           UsuarioRepository usuarioRepository,
                           AnimalRepository animalRepository) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.animalRepository = animalRepository;
    }

    /* =========================
       CRIAR DENÚNCIA
    ========================= */
    public DenunciaResponseDTO criar(DenunciaRequestDTO dto) {

        Usuario denunciante = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário denunciante não encontrado"));

        Denuncia denuncia = new Denuncia();
        denuncia.setUsuario(denunciante);
        denuncia.setCategoria(dto.getCategoria());
        denuncia.setDescricao(dto.getDescricao());
        denuncia.setStatus(StatusDenuncia.PENDENTE);
        denuncia.setCriadoEm(Instant.now());

        if (dto.getAnimalId() != null) {
            Animal animal = animalRepository.findById(dto.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
            denuncia.setAnimal(animal);
        }

        if (dto.getUsuarioDenunciadoId() != null) {
            Usuario denunciado = usuarioRepository.findById(dto.getUsuarioDenunciadoId())
                    .orElseThrow(() -> new RuntimeException("Usuário denunciado não encontrado"));
            denuncia.setUsuarioDenunciado(denunciado);
        }

        return toResponseDTO(denunciaRepository.save(denuncia));
    }

    /* =========================
       LISTAGENS
    ========================= */
    public List<DenunciaResponseDTO> listarTodas() {
        return denunciaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<DenunciaResponseDTO> listarPorStatus(StatusDenuncia status) {
        return denunciaRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* =========================
       ATUALIZAR STATUS
    ========================= */
    public void atualizarStatus(String id, StatusDenuncia status) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

        denuncia.setStatus(status);
        denuncia.setAtualizadoEm(Instant.now());
        denunciaRepository.save(denuncia);
    }

    /* =========================
       MAPPER
    ========================= */
    private DenunciaResponseDTO toResponseDTO(Denuncia denuncia) {
        DenunciaResponseDTO dto = new DenunciaResponseDTO();
        dto.setId(denuncia.getId());
        dto.setCategoria(denuncia.getCategoria());
        dto.setDescricao(denuncia.getDescricao());
        dto.setStatus(denuncia.getStatus());
        dto.setCriadoEm(denuncia.getCriadoEm());
        dto.setAtualizadoEm(denuncia.getAtualizadoEm());

        UsuarioResumoDTO denunciante = new UsuarioResumoDTO();
        denunciante.setId(denuncia.getUsuario().getId());
        denunciante.setNomeCompleto(denuncia.getUsuario().getNomeCompleto());
        dto.setUsuario(denunciante);

        if (denuncia.getUsuarioDenunciado() != null) {
            UsuarioResumoDTO denunciado = new UsuarioResumoDTO();
            denunciado.setId(denuncia.getUsuarioDenunciado().getId());
            denunciado.setNomeCompleto(denuncia.getUsuarioDenunciado().getNomeCompleto());
            dto.setUsuarioDenunciado(denunciado);
        }

        if (denuncia.getAnimal() != null) {
            AnimalResumoDTO animal = new AnimalResumoDTO();
            animal.setId(denuncia.getAnimal().getId());
            animal.setNome(denuncia.getAnimal().getNome());
            dto.setAnimal(animal);
        }

        return dto;
    }
}
