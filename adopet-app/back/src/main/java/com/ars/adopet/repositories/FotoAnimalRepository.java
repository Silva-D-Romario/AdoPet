package com.ars.adopet.repositories;

import com.ars.adopet.models.Animal;
import com.ars.adopet.models.FotoAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoAnimalRepository extends JpaRepository<FotoAnimal, String> {

    List<FotoAnimal> findByAnimal(Animal animal);
}
