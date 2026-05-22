package com.noc.smartnoc.service;

import com.noc.smartnoc.model.Incidencia;
import com.noc.smartnoc.model.Incidencia.EstadoIncidencia;
import com.noc.smartnoc.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    /** Retorna todas las incidencias */
    public List<Incidencia> findAll() {
        return incidenciaRepository.findAll();
    }

    /** Busca por ID */
    public Optional<Incidencia> findById(Long id) {
        return incidenciaRepository.findById(id);
    }

    /** Filtra por estado */
    public List<Incidencia> findByEstado(EstadoIncidencia estado) {
        return incidenciaRepository.findByEstadoOrderByFechaAperturaDesc(estado);
    }

    /** Crea una nueva incidencia generando el código automáticamente */
    public Incidencia crear(Incidencia incidencia) {
        incidencia.setCodigo(generarCodigo());
        incidencia.setFechaApertura(LocalDateTime.now());
        return incidenciaRepository.save(incidencia);
    }

    /** Actualiza estado y calcula TTR si se resuelve */
    public Optional<Incidencia> actualizarEstado(Long id, EstadoIncidencia nuevoEstado) {
        return incidenciaRepository.findById(id).map(inc -> {
            inc.setEstado(nuevoEstado);
            if (nuevoEstado == EstadoIncidencia.RESUELTA || nuevoEstado == EstadoIncidencia.CERRADA) {
                inc.setFechaCierre(LocalDateTime.now());
                if (inc.getFechaApertura() != null) {
                    long minutos = java.time.Duration.between(inc.getFechaApertura(), inc.getFechaCierre()).toMinutes();
                    inc.setTtrMinutos((int) minutos);
                }
            }
            return incidenciaRepository.save(inc);
        });
    }

    /** Elimina una incidencia */
    public void eliminar(Long id) {
        incidenciaRepository.deleteById(id);
    }

    /** KPI: MTTR promedio en minutos */
    public Double getMttrPromedio() {
        Double mttr = incidenciaRepository.calcularMttrPromedio();
        return mttr != null ? Math.round(mttr * 10.0) / 10.0 : 0.0;
    }

    /** KPI: Total incidencias activas */
    public long getIncidenciasActivas() {
        return incidenciaRepository.countIncidenciasActivas();
    }

    /** Genera código único tipo INC-YYYYMMDD-NNNNN */
    private String generarCodigo() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long total = incidenciaRepository.count() + 1;
        return String.format("INC-%s-%05d", fecha, total);
    }
}
