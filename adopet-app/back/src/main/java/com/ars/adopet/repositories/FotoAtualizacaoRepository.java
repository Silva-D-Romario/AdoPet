package com.ars.adopet.repositories;

import com.ars.adopet.models.AtualizacaoPosAdocao;
import com.ars.adopet.models.FotoAtualizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoAtualizacaoRepository extends JpaRepository<FotoAtualizacao, String> {

    List<FotoAtualizacao> findByAtualizacao(AtualizacaoPosAdocao atualizacao);
}
