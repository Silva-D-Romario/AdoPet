package com.ars.adopet.services;

import com.ars.adopet.dtos.*;
import com.ars.adopet.enums.UserRole;
import com.ars.adopet.models.Endereco;
import com.ars.adopet.models.Usuario;
import com.ars.adopet.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* =========================
       CREATE
    ========================= */
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        usuario.setTelefone(dto.getTelefone());
        usuario.setPapel(dto.getPapel());

        if (dto.getEndereco() != null) {
            usuario.setEndereco(toEndereco(dto.getEndereco()));
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponseDTO(salvo);
    }

    /* =========================
       LOGIN
    ========================= */
    public UsuarioResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        return toResponseDTO(usuario);
    }

    /* =========================
       READ
    ========================= */
    public List<UsuarioResponseDTO> listarSemAdmin() {
        return usuarioRepository.findByPapelNot(UserRole.ADMINISTRADOR)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<UsuarioResponseDTO> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeCompletoContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toResponseDTO(usuario);
    }

    /* =========================
       UPDATE
    ========================= */

    public UsuarioResponseDTO atualizarUsuario(String id, UsuarioUpdateDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.getNomeCompleto() != null) {
            usuario.setNomeCompleto(dto.getNomeCompleto());
        }

        if (dto.getTelefone() != null) {
            usuario.setTelefone(dto.getTelefone());
        }

        if (dto.getEndereco() != null) {
            usuario.setEndereco(toEndereco(dto.getEndereco()));
        }

        usuario.setAtualizadoEm(Instant.now());

        Usuario atualizado = usuarioRepository.save(usuario);
        return toResponseDTO(atualizado);
    }


    public void atualizarSenha(String id, AtualizarSenhaDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenhaHash())) {
            throw new RuntimeException("Senha atual inválida");
        }

        usuario.setSenhaHash(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.setAtualizadoEm(Instant.now());

        usuarioRepository.save(usuario);
    }



    /* =========================
       DELETE
    ========================= */
    public void deletarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }

    /* =========================
       MAPPERS
    ========================= */
    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setVerificado(usuario.isVerificado());
        dto.setPapel(usuario.getPapel());

        if (usuario.getEndereco() != null) {
            dto.setEndereco(toEnderecoDTO(usuario.getEndereco()));
        }
        return dto;
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        return new Endereco(
                dto.getRua(),
                dto.getNumero(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getCep()
        );
    }

    private EnderecoDTO toEnderecoDTO(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setRua(endereco.getRua());
        dto.setNumero(endereco.getNumero());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        return dto;
    }
}
