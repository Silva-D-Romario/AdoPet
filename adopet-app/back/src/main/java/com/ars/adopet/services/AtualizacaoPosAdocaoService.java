package com.ars.adopet.services;

import com.ars.adopet.dtos.AtualizacaoPosAdocaoRequestDTO;
import com.ars.adopet.dtos.AtualizacaoPosAdocaoResponseDTO;
import com.ars.adopet.models.*;
import com.ars.adopet.repositories.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AtualizacaoPosAdocaoService {

    private final AtualizacaoPosAdocaoRepository atualizacaoRepository;
    private final FotoAtualizacaoRepository fotoRepository;
    private final AdocaoRepository adocaoRepository;

    public AtualizacaoPosAdocaoService(
            AtualizacaoPosAdocaoRepository atualizacaoRepository,
            FotoAtualizacaoRepository fotoRepository,
            AdocaoRepository adocaoRepository) {
        this.atualizacaoRepository = atualizacaoRepository;
        this.fotoRepository = fotoRepository;
        this.adocaoRepository = adocaoRepository;
    }

    /* =========================
       CREATE
    ========================= */
    public AtualizacaoPosAdocaoResponseDTO criar(
            AtualizacaoPosAdocaoRequestDTO dto) {

        Adocao adocao = adocaoRepository.findById(dto.getAdocaoId())
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        // regra de negócio
        if (adocao.getDataAdocao() == null) {
            throw new RuntimeException(
                    "Adoção ainda não concluída");
        }

        AtualizacaoPosAdocao atualizacao = new AtualizacaoPosAdocao();
        atualizacao.setAdocao(adocao);
        atualizacao.setDescricao(dto.getDescricao());
        atualizacao.setCriadoEm(Instant.now());

        if (dto.getFotos() != null) {
            atualizacao.setFotos(mapFotos(dto.getFotos(), atualizacao));
        }

        AtualizacaoPosAdocao salva =
                atualizacaoRepository.save(atualizacao);

        return toResponseDTO(salva);
    }

    /* =========================
       READ
    ========================= */
    public List<AtualizacaoPosAdocaoResponseDTO>
    listarPorAdocao(String adocaoId) {

        Adocao adocao = adocaoRepository.findById(adocaoId)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        return atualizacaoRepository
                .findByAdocaoOrderByCriadoEmDesc(adocao)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* =========================
       DELETE (opcional)
    ========================= */
    public void deletar(String id) {
        AtualizacaoPosAdocao atualizacao =
                atualizacaoRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Atualização não encontrada"));

        atualizacaoRepository.delete(atualizacao);
    }

    /* =========================
       HELPERS
    ========================= */
    private List<FotoAtualizacao> mapFotos(
            List<String> urls,
            AtualizacaoPosAdocao atualizacao) {

        return urls.stream()
                .map(url -> {
                    FotoAtualizacao foto = new FotoAtualizacao();
                    foto.setUrl(url);
                    foto.setAtualizacao(atualizacao);
                    return foto;
                })
                .toList();
    }

    private AtualizacaoPosAdocaoResponseDTO
    toResponseDTO(AtualizacaoPosAdocao atualizacao) {

        AtualizacaoPosAdocaoResponseDTO dto =
                new AtualizacaoPosAdocaoResponseDTO();

        dto.setId(atualizacao.getId());
        dto.setDescricao(atualizacao.getDescricao());
        dto.setCriadoEm(atualizacao.getCriadoEm());

        dto.setFotos(
                atualizacao.getFotos()
                        .stream()
                        .map(FotoAtualizacao::getUrl)
                        .toList()
        );

        return dto;
    }
}
