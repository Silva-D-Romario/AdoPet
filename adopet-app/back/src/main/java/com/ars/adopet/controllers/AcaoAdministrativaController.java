package com.ars.adopet.controllers;

import com.ars.adopet.dtos.AcaoAdministrativaRequestDTO;
import com.ars.adopet.dtos.AcaoAdministrativaResponseDTO;
import com.ars.adopet.services.AcaoAdministrativaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/acoes-administrativas")
public class AcaoAdministrativaController {

    private final AcaoAdministrativaService service;

    public AcaoAdministrativaController(AcaoAdministrativaService service) {
        this.service = service;
    }

    /* =========================
       APLICAR AÇÃO
    ========================= */
    @PostMapping
    public ResponseEntity<AcaoAdministrativaResponseDTO> aplicar(
            @RequestBody @Valid AcaoAdministrativaRequestDTO dto) {

        AcaoAdministrativaResponseDTO response = service.aplicar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
