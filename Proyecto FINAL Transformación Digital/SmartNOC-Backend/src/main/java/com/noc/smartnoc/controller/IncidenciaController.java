package com.noc.smartnoc.controller;

import com.noc.smartnoc.model.Incidencia;
import com.noc.smartnoc.model.Incidencia.EstadoIncidencia;
import com.noc.smartnoc.service.IncidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/incidencias")
@CrossOrigin(origins = "*")
@Tag(name = "Incidencias", description = "Gestión de incidencias del NOC")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @GetMapping
    @Operation(summary = "Listar todas las incidencias",
               description = "Devuelve todas las incidencias registradas en el NOC.")
    public ResponseEntity<List<Incidencia>> listarTodas() {
        return ResponseEntity.ok(incidenciaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener incidencia por ID")
    @ApiResponse(responseCode = "404", description = "Incidencia no encontrada")
    public ResponseEntity<Incidencia> obtenerPorId(
            @Parameter(description = "ID de la incidencia") @PathVariable Long id) {
        return incidenciaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Filtrar incidencias por estado",
               description = "Estados válidos: ABIERTA, EN_PROGRESO, RESUELTA, CERRADA")
    public ResponseEntity<List<Incidencia>> porEstado(@PathVariable EstadoIncidencia estado) {
        return ResponseEntity.ok(incidenciaService.findByEstado(estado));
    }

    @PostMapping
    @Operation(summary = "Crear nueva incidencia",
               description = "El código se genera automáticamente. Requiere título y severidad.")
    @ApiResponse(responseCode = "201", description = "Incidencia creada correctamente")
    public ResponseEntity<Incidencia> crear(@Valid @RequestBody Incidencia incidencia) {
        Incidencia nueva = incidenciaService.crear(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de una incidencia",
               description = "Cambia el estado. Si pasa a RESUELTA, calcula el TTR automáticamente.")
    public ResponseEntity<Incidencia> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        EstadoIncidencia nuevoEstado = EstadoIncidencia.valueOf(body.get("estado"));
        return incidenciaService.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar incidencia")
    @ApiResponse(responseCode = "204", description = "Incidencia eliminada")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        incidenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/kpis")
    @Operation(summary = "KPIs del NOC",
               description = "Devuelve MTTR promedio e incidencias activas en tiempo real.")
    public ResponseEntity<Map<String, Object>> kpis() {
        return ResponseEntity.ok(Map.of(
            "mttrPromedioMinutos",  incidenciaService.getMttrPromedio(),
            "incidenciasActivas",   incidenciaService.getIncidenciasActivas()
        ));
    }
}
