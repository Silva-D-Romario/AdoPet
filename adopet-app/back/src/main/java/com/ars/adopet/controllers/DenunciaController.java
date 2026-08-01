package com.ars.adopet.controllers;

import com.ars.adopet.dtos.DenunciaRequestDTO;
import com.ars.adopet.dtos.DenunciaResponseDTO;
import com.ars.adopet.enums.StatusDenuncia;
import com.ars.adopet.services.DenunciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {

    private final DenunciaService service;

    public DenunciaController(DenunciaService service) {
        this.service = service;
    }

    /* =========================
       CRIAR DENÚNCIA
    ========================= */
    @PostMapping("/criar")
    public ResponseEntity<DenunciaResponseDTO> criar(
            @RequestBody DenunciaRequestDTO dto) {

        return ResponseEntity.ok(service.criar(dto));
    }

    /* =========================
       LISTAR TODAS
    ========================= */
    @GetMapping("/listar")
    public ResponseEntity<List<DenunciaResponseDTO>> listar() {

        return ResponseEntity.ok(service.listarTodas());
    }

    /* =========================
       LISTAR POR STATUS
    ========================= */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DenunciaResponseDTO>> listarPorStatus(
            @PathVariable StatusDenuncia status) {

        return ResponseEntity.ok(service.listarPorStatus(status));
    }
}
