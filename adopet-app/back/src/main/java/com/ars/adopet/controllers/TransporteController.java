package com.ars.adopet.controllers;

import com.ars.adopet.dtos.TransporteResponseDTO;
import com.ars.adopet.services.TransporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportes")
public class TransporteController {

    private final TransporteService transporteService;

    public TransporteController(TransporteService transporteService) {
        this.transporteService = transporteService;
    }

    /* =========================
       SOLICITAR TRANSPORTE
    ========================= */
    @PostMapping("/adocao/{adocaoId}")
    public ResponseEntity<TransporteResponseDTO> solicitar(@PathVariable String adocaoId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transporteService.solicitarTransporte(adocaoId));
    }

    /* =========================
       LISTAR TODOS OS TRANSPORTES
    ========================= */
    @GetMapping("/listar")
    public ResponseEntity<List<TransporteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(transporteService.listarTodos());
    }

    /* =========================
       CONSULTAR TRANSPORTE
    ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<TransporteResponseDTO> buscar(@PathVariable String id) {
        return ResponseEntity.ok(transporteService.buscarPorId(id));
    }
}
