package com.ars.adopet.repositories;

import com.ars.adopet.enums.ReportStatus;
import com.ars.adopet.models.RastreamentoTransporte;
import com.ars.adopet.models.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RastreamentoTransporteRepository extends JpaRepository<RastreamentoTransporte, String> {

    List<RastreamentoTransporte> findByTransporte(Transporte transporte);

    List<RastreamentoTransporte> findByStatus(ReportStatus status);

    List<RastreamentoTransporte>
    findByTransporteIdOrderByCriadoEmDesc(String transporteId);
}
