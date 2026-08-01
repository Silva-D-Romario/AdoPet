package com.ars.adopet.repositories;

import com.ars.adopet.models.Adocao;
import com.ars.adopet.models.SolicitacaoAdocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdocaoRepository extends JpaRepository<Adocao, String> {

    Optional<Adocao> findBySolicitacao(SolicitacaoAdocao solicitacao);
}
