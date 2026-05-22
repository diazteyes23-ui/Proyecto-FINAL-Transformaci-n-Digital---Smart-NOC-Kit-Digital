package com.noc.smartnoc.controller;

import com.noc.smartnoc.model.OrdenTrabajo;
import com.noc.smartnoc.model.OrdenTrabajo.EstadoOrden;
import com.noc.smartnoc.service.OrdenTrabajoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ordenes-trabajo")
@CrossOrigin(origins = "*")
@Tag(name = "Órdenes de Trabajo", description = "Field Service Management – despacho de técnicos")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes de trabajo")
    public List<OrdenTrabajo> listarTodas() {
        return ordenTrabajoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden de trabajo por ID")
    public ResponseEntity<OrdenTrabajo> obtenerPorId(@PathVariable Long id) {
        return ordenTrabajoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Filtrar órdenes por estado",
               description = "Estados: PENDIENTE, DESPACHADA, EN_CAMINO, EN_PROGRESO, COMPLETADA, CANCELADA")
    public List<OrdenTrabajo> porEstado(@PathVariable EstadoOrden estado) {
        return ordenTrabajoService.findByEstado(estado);
    }

    @GetMapping("/tecnico/{tecnicoId}")
    @Operation(summary = "Órdenes asignadas a un técnico específico")
    public List<OrdenTrabajo> porTecnico(@PathVariable Long tecnicoId) {
        return ordenTrabajoService.findByTecnico(tecnicoId);
    }

    @PostMapping
    @Operation(summary = "Crear y despachar orden de trabajo",
               description = "El código WO se genera automáticamente. Incluye incidencia y técnico asignado.")
    public ResponseEntity<OrdenTrabajo> crear(@Valid @RequestBody OrdenTrabajo orden) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenTrabajoService.crear(orden));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de la orden de trabajo")
    public ResponseEntity<OrdenTrabajo> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        EstadoOrden nuevoEstado = EstadoOrden.valueOf(body.get("estado"));
        return ordenTrabajoService.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar / eliminar orden de trabajo")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenTrabajoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
