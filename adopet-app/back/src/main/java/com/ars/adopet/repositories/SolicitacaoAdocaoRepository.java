package com.ars.adopet.repositories;

import com.ars.adopet.enums.RequestStatus;
import com.ars.adopet.models.Animal;
import com.ars.adopet.models.SolicitacaoAdocao;
import com.ars.adopet.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoAdocaoRepository extends JpaRepository<SolicitacaoAdocao, String> {

    List<SolicitacaoAdocao> findBySolicitante(Usuario solicitante);

    // Busca todas as solicitações feitas por um usuário específico
    List<SolicitacaoAdocao> findBySolicitanteId(String solicitanteId);

    // Busca todas as solicitações enviadas para os animais de um doador específico
    List<SolicitacaoAdocao> findByAnimalDoadorId(String doadorId);

    List<SolicitacaoAdocao> findByAnimal(Animal animal);

    List<SolicitacaoAdocao> findByStatus(RequestStatus status);
}
