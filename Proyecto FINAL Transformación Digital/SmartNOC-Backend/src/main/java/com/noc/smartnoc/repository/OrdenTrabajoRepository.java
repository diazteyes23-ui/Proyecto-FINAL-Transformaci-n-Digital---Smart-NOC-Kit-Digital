package com.noc.smartnoc.repository;

import com.noc.smartnoc.model.OrdenTrabajo;
import com.noc.smartnoc.model.OrdenTrabajo.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {

    List<OrdenTrabajo> findByEstado(EstadoOrden estado);

    List<OrdenTrabajo> findByTecnicoId(Long tecnicoId);

    List<OrdenTrabajo> findByIncidenciaId(Long incidenciaId);

    boolean existsByCodigo(String codigo);
}
