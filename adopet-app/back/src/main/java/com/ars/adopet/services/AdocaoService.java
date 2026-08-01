package com.ars.adopet.services;

import com.ars.adopet.dtos.*;
import com.ars.adopet.enums.AdoptionStatus;
import com.ars.adopet.enums.RequestStatus;
import com.ars.adopet.models.*;
import com.ars.adopet.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;

@Service
public class AdocaoService {

    private final SolicitacaoAdocaoRepository solicitacaoRepository;
    private final AdocaoRepository adocaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnimalRepository animalRepository;

    public AdocaoService(SolicitacaoAdocaoRepository solicitacaoRepository,
                         AdocaoRepository adocaoRepository,
                         UsuarioRepository usuarioRepository,
                         AnimalRepository animalRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.adocaoRepository = adocaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.animalRepository = animalRepository;
    }

    /* =========================
       CRIAR SOLICITAÇÃO
    ========================= */
    public SolicitacaoAdocaoResponseDTO solicitarAdocao(SolicitacaoAdocaoRequestDTO dto) {

        Usuario solicitante = usuarioRepository.findById(dto.getSolicitanteId())
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado"));

        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));

        if (animal.getStatus() != AdoptionStatus.DISPONIVEL) {
            throw new RuntimeException("Animal não está disponível para adoção");
        }

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao();
        solicitacao.setSolicitante(solicitante);
        solicitacao.setAnimal(animal);
        solicitacao.setStatus(RequestStatus.PENDENTE);
        solicitacao.setCriadoEm(Instant.now());

        return toSolicitacaoResponseDTO(
                solicitacaoRepository.save(solicitacao)
        );
    }

     /* =========================
    LISTAR TODAS AS SOLICITAÇÕES
    ========================= */
     public List<SolicitacaoAdocaoResponseDTO> listarTodas() {
         return solicitacaoRepository.findAll()
                 .stream()
                 .map(this::toSolicitacaoResponseDTO)
                 .toList();
     }

    /* =========================
      LISTAR MINHAS SOLICITAÇÕES (ENVIADAS)
    ========================= */
    public List<SolicitacaoAdocaoResponseDTO> listarPorSolicitante(String id) {
        return solicitacaoRepository.findBySolicitanteId(id)
                .stream()
                .map(this::toSolicitacaoResponseDTO)
                .toList();
    }

    /* =========================
       LISTAR SOLICITAÇÕES RECEBIDAS (POR DOADOR)
    ========================= */
    public List<SolicitacaoAdocaoResponseDTO> listarPorDoador(String id) {
        // Busca os animais que pertencem ao doador e então suas solicitações
        return solicitacaoRepository.findByAnimalDoadorId(id)
                .stream()
                .map(this::toSolicitacaoResponseDTO)
                .toList();
    }

    /* =========================
       APROVAR SOLICITAÇÃO
    ========================= */
    public AdocaoResponseDTO aprovarSolicitacao(String solicitacaoId) {

        SolicitacaoAdocao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        if (solicitacao.getStatus() != RequestStatus.PENDENTE) {
            throw new RuntimeException("Solicitação já foi processada");
        }

        // Atualiza solicitação
        solicitacao.setStatus(RequestStatus.APROVADA);
        solicitacao.setAtualizadoEm(Instant.now());

        // Atualiza animal
        Animal animal = solicitacao.getAnimal();
        animal.setStatus(AdoptionStatus.ADOTADO);

        // Cria adoção
        Adocao adocao = new Adocao();
        adocao.setSolicitacao(solicitacao);
        adocao.setDataAdocao(Instant.now());

        solicitacaoRepository.save(solicitacao);
        animalRepository.save(animal);

        return toAdocaoResponseDTO(adocaoRepository.save(adocao));
    }

    /* =========================
       REJEITAR SOLICITAÇÃO
    ========================= */
    public SolicitacaoAdocaoResponseDTO rejeitarSolicitacao(String solicitacaoId) {

        SolicitacaoAdocao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        solicitacao.setStatus(RequestStatus.RECUSADA);
        solicitacao.setAtualizadoEm(Instant.now());

        return toSolicitacaoResponseDTO(
                solicitacaoRepository.save(solicitacao)
        );
    }

    /* =========================
       MAPPERS
    ========================= */

    private SolicitacaoAdocaoResponseDTO toSolicitacaoResponseDTO(SolicitacaoAdocao s) {

        SolicitacaoAdocaoResponseDTO dto = new SolicitacaoAdocaoResponseDTO();
        dto.setId(s.getId());
        dto.setCriadoEm(s.getCriadoEm());
        dto.setAtualizadoEm(s.getAtualizadoEm());
        dto.setStatus(s.getStatus());

        UsuarioResumoDTO usuario = new UsuarioResumoDTO();
        usuario.setId(s.getSolicitante().getId());
        usuario.setNomeCompleto(s.getSolicitante().getNomeCompleto());

        AnimalResumoDTO animal = new AnimalResumoDTO();
        animal.setId(s.getAnimal().getId());
        animal.setNome(s.getAnimal().getNome());

        dto.setSolicitante(usuario);
        dto.setAnimal(animal);

        return dto;
    }

    private AdocaoResponseDTO toAdocaoResponseDTO(Adocao adocao) {

        AdocaoResponseDTO dto = new AdocaoResponseDTO();
        dto.setId(adocao.getId());
        dto.setDataAdocao(adocao.getDataAdocao());
        dto.setSolicitacao(
                toSolicitacaoResponseDTO(adocao.getSolicitacao())
        );
        return dto;
    }
}
