package com.noc.smartnoc.repository;

import com.noc.smartnoc.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    List<Tecnico> findByDisponible(Boolean disponible);

    List<Tecnico> findByZona(String zona);

    Optional<Tecnico> findByEmail(String email);

    List<Tecnico> findBySkillsContainingAndDisponible(String skill, Boolean disponible);
}
