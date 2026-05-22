package com.noc.smartnoc.repository;

import com.noc.smartnoc.model.Equipo;
import com.noc.smartnoc.model.Equipo.EstadoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByEstado(EstadoEquipo estado);

    List<Equipo> findByZona(String zona);

    List<Equipo> findByTipo(Equipo.TipoEquipo tipo);
}
