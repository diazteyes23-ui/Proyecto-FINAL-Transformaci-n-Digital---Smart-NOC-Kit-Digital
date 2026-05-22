package com.noc.smartnoc.service;

import com.noc.smartnoc.model.Equipo;
import com.noc.smartnoc.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    public List<Equipo> findAll() {
        return equipoRepository.findAll();
    }

    public Equipo findById(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado"));
    }

    public List<Equipo> findByEstado(Equipo.EstadoEquipo estado) {
        return equipoRepository.findByEstado(estado);
    }

    public List<Equipo> findByTipo(Equipo.TipoEquipo tipo) {
        return equipoRepository.findByTipo(tipo);
    }

    public Equipo create(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    public Equipo updateEstado(Long id, Equipo.EstadoEquipo nuevoEstado) {
        Equipo equipo = findById(id);
        equipo.setEstado(nuevoEstado);
        return equipoRepository.save(equipo);
    }
}
