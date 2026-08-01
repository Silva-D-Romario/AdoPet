package com.ars.adopet.services;

import com.ars.adopet.dtos.*;
import com.ars.adopet.enums.ReportStatus;
import com.ars.adopet.models.*;
import com.ars.adopet.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.List;

@Service
public class TransporteService {

    private final TransporteRepository transporteRepository;
    private final RastreamentoTransporteRepository rastreamentoRepository;
    private final AdocaoRepository adocaoRepository;

    public TransporteService(TransporteRepository transporteRepository,
                             RastreamentoTransporteRepository rastreamentoRepository,
                             AdocaoRepository adocaoRepository) {
        this.transporteRepository = transporteRepository;
        this.rastreamentoRepository = rastreamentoRepository;
        this.adocaoRepository = adocaoRepository;
    }

    /* =========================
       SOLICITAR TRANSPORTE
    ========================= */
    public TransporteResponseDTO solicitarTransporte(String adocaoId) {

        Adocao adocao = adocaoRepository.findById(adocaoId)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        if (transporteRepository.findByAdocao(adocao).isPresent()) {
            throw new RuntimeException("Transporte já solicitado para esta adoção");
        }

        Transporte transporte = new Transporte();
        transporte.setAdocao(adocao);

        Transporte salvo = transporteRepository.save(transporte);

        criarRastreamento(salvo, ReportStatus.PENDENTE, "Transporte solicitado");

        return toResponseDTO(salvo);
    }

    /* =========================
    LISTAR TODOS OS TRANSPORTES
    ========================= */
    public List<TransporteResponseDTO> listarTodos() {
        return transporteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    /* =========================
       CONSULTAR TRANSPORTE
    ========================= */
    public TransporteResponseDTO buscarPorId(String id) {

        Transporte transporte = transporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transporte não encontrado"));

        return toResponseDTO(transporte);
    }

    /* =========================
       SIMULADOR DE RASTREAMENTO - roda a cada 10 segundos
    ========================= */
    @Scheduled(fixedRate = 10000)
    public void simularRastreamento() {

        List<Transporte> transportes = transporteRepository.findAll();

        for (Transporte transporte : transportes) {

            List<RastreamentoTransporte> rastreamentos =
                    rastreamentoRepository
                            .findByTransporteIdOrderByCriadoEmDesc(transporte.getId());

            ReportStatus ultimoStatus = rastreamentos.isEmpty()
                    ? null
                    : rastreamentos.get(0).getStatus();

            if (ultimoStatus == null) {
                criarRastreamento(transporte,
                        ReportStatus.PENDENTE,
                        "Aguardando coleta");
            }
            else if (ultimoStatus == ReportStatus.PENDENTE) {
                criarRastreamento(transporte,
                        ReportStatus.A_CAMINHO,
                        "Animal a caminho do adotante");
            }
            else if (ultimoStatus == ReportStatus.A_CAMINHO) {
                criarRastreamento(transporte,
                        ReportStatus.ENTREGUE,
                        "Animal entregue com sucesso");
            }
        }
    }


    /* =========================
       HELPERS
    ========================= */
    private void criarRastreamento(Transporte transporte,
                                   ReportStatus status,
                                   String mensagem) {

        boolean entregue = rastreamentoRepository
                .findByTransporteIdOrderByCriadoEmDesc(transporte.getId())
                .stream()
                .anyMatch(r -> r.getStatus() == ReportStatus.ENTREGUE);

        if (entregue) return;

        RastreamentoTransporte rastreio = new RastreamentoTransporte();
        rastreio.setTransporte(transporte);
        rastreio.setStatus(status);
        rastreio.setMensagem(mensagem);
        rastreio.setCriadoEm(Instant.now());

        rastreamentoRepository.save(rastreio);
    }


    /* =========================
       MAPPER
    ========================= */
    private TransporteResponseDTO toResponseDTO(Transporte transporte) {

        TransporteResponseDTO dto = new TransporteResponseDTO();
        dto.setId(transporte.getId());
        dto.setCriadoEm(transporte.getCriadoEm());

        AdocaoResponseDTO adocao = new AdocaoResponseDTO();
        adocao.setId(transporte.getAdocao().getId());
        adocao.setDataAdocao(transporte.getAdocao().getDataAdocao());
        dto.setAdocao(adocao);

        dto.setRastreamentos(
                rastreamentoRepository
                        .findByTransporteIdOrderByCriadoEmDesc(transporte.getId())
                        .stream()
                        .map(r -> {
                            RastreamentoTransporteDTO rDto = new RastreamentoTransporteDTO();
                            rDto.setId(r.getId());
                            rDto.setStatus(r.getStatus());
                            rDto.setMensagem(r.getMensagem());
                            rDto.setCriadoEm(r.getCriadoEm());
                            return rDto;
                        })
                        .toList()
        );

        return dto;
    }
}
