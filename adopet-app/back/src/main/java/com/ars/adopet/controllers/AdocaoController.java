package com.ars.adopet.controllers;

import com.ars.adopet.dtos.*;
import com.ars.adopet.services.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    private final AdocaoService adocaoService;

    public AdocaoController(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    /* =========================
       SOLICITAR ADOÇÃO
    ========================= */
    @PostMapping("/solicitar")
    public ResponseEntity<SolicitacaoAdocaoResponseDTO> solicitar(
            @RequestBody @Valid SolicitacaoAdocaoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adocaoService.solicitarAdocao(dto));
    }

    /* =========================
    LISTAR TODAS AS SOLICITAÇÕES
    ========================= */
    @GetMapping("/listar")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(adocaoService.listarTodas());
    }

    /* =========================
       LISTAR MINHAS SOLICITAÇÕES (ENVIADAS)
    ========================= */
    @GetMapping("/solicitante/{id}")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorSolicitante(@PathVariable String id) {
        return ResponseEntity.ok(adocaoService.listarPorSolicitante(id));
    }

    /* =========================
       LISTAR SOLICITAÇÕES RECEBIDAS (POR DOADOR)
    ========================= */
    @GetMapping("/doador/{id}")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorDorado(@PathVariable String id) {
        return ResponseEntity.ok(adocaoService.listarPorDoador(id));
    }

    /* =========================
       APROVAR
    ========================= */
    @PostMapping("/solicitacoes/{id}/aprovar")
    public ResponseEntity<AdocaoResponseDTO> aprovar(@PathVariable String id) {
        return ResponseEntity.ok(adocaoService.aprovarSolicitacao(id));
    }

    /* =========================
       REJEITAR
    ========================= */
    @PostMapping("/solicitacoes/{id}/rejeitar")
    public ResponseEntity<SolicitacaoAdocaoResponseDTO> rejeitar(@PathVariable String id) {
        return ResponseEntity.ok(adocaoService.rejeitarSolicitacao(id));
    }
}
