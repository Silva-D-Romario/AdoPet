package com.ars.adopet.controllers;

import com.ars.adopet.dtos.AnimalRequestDTO;
import com.ars.adopet.dtos.AnimalResponseDTO;
import com.ars.adopet.services.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /* =========================
       POST
    ========================= */
    @PostMapping("/criar")
    public ResponseEntity<AnimalResponseDTO> criar(@RequestBody @Valid AnimalRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalService.criar(dto));
    }

    /* =========================
       GET
    ========================= */
    @GetMapping("/listar")
    public ResponseEntity<List<AnimalResponseDTO>> listarTodos() {
        return ResponseEntity.ok(animalService.listarTodos());
    }

    @GetMapping("/doador/{doadorId}")
    public ResponseEntity<List<AnimalResponseDTO>> listarPorDoador(@PathVariable String doadorId) {
        List<AnimalResponseDTO> animais = animalService.listarPorDoador(doadorId);
        return ResponseEntity.ok(animais);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<AnimalResponseDTO>> listarDisponiveis() {
        return ResponseEntity.ok(animalService.listarDisponiveis());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AnimalResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(animalService.buscarPorNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(animalService.buscarPorId(id));
    }

    /* =========================
       PUT
    ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> atualizar(
            @PathVariable String id,
            @RequestBody AnimalRequestDTO dto) {

        return ResponseEntity.ok(animalService.atualizar(id, dto));
    }

    /* =========================
       DELETE
    ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable String id) {
        animalService.deletar(id);
        return ResponseEntity.ok("Animal removido com sucesso");
    }
}
