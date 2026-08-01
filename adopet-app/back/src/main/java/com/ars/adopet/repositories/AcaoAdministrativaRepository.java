package com.ars.adopet.repositories;

import com.ars.adopet.models.AcaoAdministrativa;
import com.ars.adopet.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcaoAdministrativaRepository extends JpaRepository<AcaoAdministrativa, String> {

    List<AcaoAdministrativa> findByAdministrador(Usuario administrador);

    List<AcaoAdministrativa> findByUsuarioAlvo(Usuario usuarioAlvo);
}
