package com.ars.adopet.controllers;

import com.ars.adopet.dtos.*;
import com.ars.adopet.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /* =========================
       POST
    ========================= */
    @PostMapping("/criar")
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.criarUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.login(dto));
    }

    /* =========================
       GET
    ========================= */
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarSemAdmin() {
        return ResponseEntity.ok(usuarioService.listarSemAdmin());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(usuarioService.buscarPorNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }


    /* =========================
       UPDATE
    ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable String id,
            @RequestBody UsuarioUpdateDTO dto) {

        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, dto));
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<String> atualizarSenha(
            @PathVariable String id,
            @RequestBody AtualizarSenhaDTO dto) {

        usuarioService.atualizarSenha(id, dto);
        return ResponseEntity.ok("Senha atualizada com sucesso");
    }



    /* =========================
       DELETE
    ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemResponseDTO> deletar(@PathVariable String id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok(
                new MensagemResponseDTO("Usuário apagado com sucesso")
        );
    }
}
