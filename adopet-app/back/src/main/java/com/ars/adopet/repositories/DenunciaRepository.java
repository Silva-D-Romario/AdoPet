package com.ars.adopet.repositories;

import com.ars.adopet.enums.StatusDenuncia;
import com.ars.adopet.models.Denuncia;
import com.ars.adopet.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, String> {

    List<Denuncia> findByUsuario(Usuario usuario);

    List<Denuncia> findByStatus(StatusDenuncia status);
}
