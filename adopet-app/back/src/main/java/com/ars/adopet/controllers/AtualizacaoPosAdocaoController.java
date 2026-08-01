package com.ars.adopet.controllers;

import com.ars.adopet.dtos.AtualizacaoPosAdocaoRequestDTO;
import com.ars.adopet.dtos.AtualizacaoPosAdocaoResponseDTO;
import com.ars.adopet.services.AtualizacaoPosAdocaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atualizacoes-pos-adocao")
public class AtualizacaoPosAdocaoController {

    private final AtualizacaoPosAdocaoService service;

    public AtualizacaoPosAdocaoController(
            AtualizacaoPosAdocaoService service) {
        this.service = service;
    }

    /* =========================
       POST
    ========================= */
    @PostMapping
    public ResponseEntity<AtualizacaoPosAdocaoResponseDTO>
    criar(@RequestBody @Valid AtualizacaoPosAdocaoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(dto));
    }

    /* =========================
       GET
    ========================= */
    @GetMapping("/adocao/{adocaoId}")
    public ResponseEntity<List<AtualizacaoPosAdocaoResponseDTO>>
    listarPorAdocao(@PathVariable String adocaoId) {

        return ResponseEntity.ok(
                service.listarPorAdocao(adocaoId));
    }

    /* =========================
       DELETE (opcional)
    ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.ok(
                "Atualização removida com sucesso");
    }
}
