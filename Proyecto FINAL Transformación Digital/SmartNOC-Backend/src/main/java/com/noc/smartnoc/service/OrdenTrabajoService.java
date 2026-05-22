package com.noc.smartnoc.service;

import com.noc.smartnoc.model.OrdenTrabajo;
import com.noc.smartnoc.model.OrdenTrabajo.EstadoOrden;
import com.noc.smartnoc.repository.OrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenTrabajoService {

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    public List<OrdenTrabajo> findAll() {
        return ordenTrabajoRepository.findAll();
    }

    public Optional<OrdenTrabajo> findById(Long id) {
        return ordenTrabajoRepository.findById(id);
    }

    public List<OrdenTrabajo> findByEstado(EstadoOrden estado) {
        return ordenTrabajoRepository.findByEstado(estado);
    }

    public List<OrdenTrabajo> findByTecnico(Long tecnicoId) {
        return ordenTrabajoRepository.findByTecnicoId(tecnicoId);
    }

    public OrdenTrabajo crear(OrdenTrabajo orden) {
        orden.setCodigo(generarCodigo());
        return ordenTrabajoRepository.save(orden);
    }

    /** Actualiza estado; si se despacha, registra fecha de despacho */
    public Optional<OrdenTrabajo> actualizarEstado(Long id, EstadoOrden nuevoEstado) {
        return ordenTrabajoRepository.findById(id).map(orden -> {
            orden.setEstado(nuevoEstado);
            if (nuevoEstado == EstadoOrden.DESPACHADA) {
                orden.setFechaDespacho(LocalDateTime.now());
            }
            if (nuevoEstado == EstadoOrden.COMPLETADA || nuevoEstado == EstadoOrden.CANCELADA) {
                orden.setFechaCierre(LocalDateTime.now());
            }
            return ordenTrabajoRepository.save(orden);
        });
    }

    public void eliminar(Long id) {
        ordenTrabajoRepository.deleteById(id);
    }

    private String generarCodigo() {
        long total = ordenTrabajoRepository.count() + 1;
        return String.format("WO-%d-%05d", LocalDateTime.now().getYear(), total);
    }
}
