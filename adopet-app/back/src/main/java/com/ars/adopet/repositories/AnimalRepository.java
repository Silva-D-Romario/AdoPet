package com.ars.adopet.repositories;

import com.ars.adopet.enums.AdoptionStatus;
import com.ars.adopet.models.Animal;
import com.ars.adopet.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, String> {

    List<Animal> findByStatus(AdoptionStatus status);

    List<Animal> findByDoador(Usuario doador);

    List<Animal> findByDoadorId(String doadorId);

    List<Animal> findByNomeContainingIgnoreCase(String nome);

}
