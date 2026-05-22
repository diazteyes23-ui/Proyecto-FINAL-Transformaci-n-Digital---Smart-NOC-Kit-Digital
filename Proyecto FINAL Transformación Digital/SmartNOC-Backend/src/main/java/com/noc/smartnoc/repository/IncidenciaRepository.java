package com.noc.smartnoc.repository;

import com.noc.smartnoc.model.Incidencia;
import com.noc.smartnoc.model.Incidencia.EstadoIncidencia;
import com.noc.smartnoc.model.Incidencia.Severidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByEstado(EstadoIncidencia estado);

    List<Incidencia> findBySeveridad(Severidad severidad);

    List<Incidencia> findByEstadoOrderByFechaAperturaDesc(EstadoIncidencia estado);

    boolean existsByCodigo(String codigo);

    @Query("SELECT COUNT(i) FROM Incidencia i WHERE i.estado = 'ABIERTA' OR i.estado = 'EN_PROGRESO'")
    long countIncidenciasActivas();

    @Query("SELECT AVG(i.ttrMinutos) FROM Incidencia i WHERE i.ttrMinutos IS NOT NULL")
    Double calcularMttrPromedio();
}
