package com.ars.adopet.repositories;

import com.ars.adopet.models.Adocao;
import com.ars.adopet.models.AtualizacaoPosAdocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtualizacaoPosAdocaoRepository extends JpaRepository<AtualizacaoPosAdocao, String> {

    List<AtualizacaoPosAdocao> findByAdocaoOrderByCriadoEmDesc(Adocao adocao);
}
