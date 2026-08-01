package com.ars.adopet.services;

import com.ars.adopet.dtos.AnimalRequestDTO;
import com.ars.adopet.dtos.AnimalResponseDTO;
import com.ars.adopet.dtos.UsuarioResumoDTO;
import com.ars.adopet.enums.AdoptionStatus;
import com.ars.adopet.models.Animal;
import com.ars.adopet.models.FotoAnimal;
import com.ars.adopet.models.Usuario;
import com.ars.adopet.repositories.AnimalRepository;
import com.ars.adopet.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;

    public AnimalService(AnimalRepository animalRepository,
                         UsuarioRepository usuarioRepository) {
        this.animalRepository = animalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /* =========================
       CREATE
    ========================= */
    public AnimalResponseDTO criar(AnimalRequestDTO dto) {

        Usuario doador = usuarioRepository.findById(dto.getDoadorId())
                .orElseThrow(() -> new RuntimeException("Doador não encontrado"));

        Animal animal = new Animal();
        animal.setNome(dto.getNome());
        animal.setEspecie(dto.getEspecie());
        animal.setRaca(dto.getRaca());
        animal.setIdade(dto.getIdade());
        animal.setVacinado(dto.isVacinado());
        animal.setCastrado(dto.isCastrado());
        animal.setStatus(dto.getStatus() != null
                ? dto.getStatus()
                : AdoptionStatus.DISPONIVEL);
        animal.setDoador(doador);

        // FOTOS
        if (dto.getFotosUrls() != null) {
            animal.setFotos(mapFotos(dto.getFotosUrls(), animal));
        }

        Animal salvo = animalRepository.save(animal);
        return toResponseDTO(salvo);
    }

    /* =========================
       READ
    ========================= */
    public List<AnimalResponseDTO> listarTodos() {
        return animalRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* =========================
       READ (Listar por Doador)
    ========================= */
    public List<AnimalResponseDTO> listarPorDoador(String doadorId) {
        if (!usuarioRepository.existsById(doadorId)) {
            throw new RuntimeException("Usuário doador não encontrado");
        }

        return animalRepository.findByDoadorId(doadorId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AnimalResponseDTO buscarPorId(String id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        return toResponseDTO(animal);
    }

    public List<AnimalResponseDTO> buscarPorNome(String nome) {
        return animalRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<AnimalResponseDTO> listarDisponiveis() {
        return animalRepository.findByStatus(AdoptionStatus.DISPONIVEL)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* =========================
       UPDATE
    ========================= */
    public AnimalResponseDTO atualizar(String id, AnimalRequestDTO dto) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));

        if (dto.getNome() != null) animal.setNome(dto.getNome());
        if (dto.getEspecie() != null) animal.setEspecie(dto.getEspecie());
        if (dto.getRaca() != null) animal.setRaca(dto.getRaca());
        if (dto.getIdade() > 0) animal.setIdade(dto.getIdade());

        animal.setVacinado(dto.isVacinado());
        animal.setCastrado(dto.isCastrado());

        if (dto.getStatus() != null) {
            animal.setStatus(dto.getStatus());
        }

        // ATUALIZA FOTOS (remove antigas e adiciona novas)
        if (dto.getFotosUrls() != null) {
            animal.getFotos().clear();
            animal.getFotos().addAll(mapFotos(dto.getFotosUrls(), animal));
        }

        animal.setAtualizadoEm(Instant.now());

        return toResponseDTO(animalRepository.save(animal));
    }


    /* =========================
       DELETE
    ========================= */
    public void deletar(String id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        animalRepository.delete(animal);
    }

    /* =========================
       MAPPERS
    ========================= */
    private List<FotoAnimal> mapFotos(List<String> urls, Animal animal) {
        if (urls == null) return List.of();

        return urls.stream()
                .map(url -> {
                    FotoAnimal foto = new FotoAnimal();
                    foto.setUrl(url);
                    foto.setAnimal(animal);
                    return foto;
                })
                .toList();
    }


    private AnimalResponseDTO toResponseDTO(Animal animal) {

        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.setId(animal.getId());
        dto.setNome(animal.getNome());
        dto.setEspecie(animal.getEspecie());
        dto.setRaca(animal.getRaca());
        dto.setIdade(animal.getIdade());
        dto.setVacinado(animal.isVacinado());
        dto.setCastrado(animal.isCastrado());
        dto.setStatus(animal.getStatus());
        dto.setCriadoEm(animal.getCriadoEm());
        dto.setAtualizadoEm(animal.getAtualizadoEm());

        if (animal.getDoador() != null) {
            UsuarioResumoDTO doador = new UsuarioResumoDTO();
            doador.setId(animal.getDoador().getId());
            doador.setNomeCompleto(animal.getDoador().getNomeCompleto());
            dto.setDoador(doador);
        }

        dto.setFotosUrls(
                animal.getFotos()
                        .stream()
                        .map(FotoAnimal::getUrl)
                        .toList()
        );

        return dto;
    }
}
