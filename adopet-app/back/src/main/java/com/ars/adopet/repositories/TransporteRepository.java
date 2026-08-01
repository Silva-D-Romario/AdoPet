package com.ars.adopet.repositories;

import com.ars.adopet.models.Adocao;
import com.ars.adopet.models.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransporteRepository extends JpaRepository<Transporte, String> {

    Optional<Transporte> findByAdocao(Adocao adocao);
}
