package com.noc.smartnoc.controller;

import com.noc.smartnoc.model.Tecnico;
import com.noc.smartnoc.repository.TecnicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tecnicos")
@CrossOrigin(origins = "*")
@Tag(name = "Técnicos", description = "Gestión del equipo de campo del NOC")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los técnicos")
    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar técnicos disponibles",
               description = "Devuelve solo los técnicos actualmente disponibles para despacho.")
    public List<Tecnico> disponibles() {
        return tecnicoRepository.findByDisponible(true);
    }

    @GetMapping("/zona/{zona}")
    @Operation(summary = "Técnicos por zona geográfica")
    public List<Tecnico> porZona(@PathVariable String zona) {
        return tecnicoRepository.findByZona(zona);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener técnico por ID")
    public ResponseEntity<Tecnico> obtenerPorId(@PathVariable Long id) {
        return tecnicoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo técnico")
    public ResponseEntity<Tecnico> crear(@Valid @RequestBody Tecnico tecnico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tecnicoRepository.save(tecnico));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos del técnico")
    public ResponseEntity<Tecnico> actualizar(@PathVariable Long id, @Valid @RequestBody Tecnico datos) {
        return tecnicoRepository.findById(id).map(t -> {
            t.setNombre(datos.getNombre());
            t.setApellidos(datos.getApellidos());
            t.setTelefono(datos.getTelefono());
            t.setSkills(datos.getSkills());
            t.setZona(datos.getZona());
            t.setDisponible(datos.getDisponible());
            t.setLatitud(datos.getLatitud());
            t.setLongitud(datos.getLongitud());
            return ResponseEntity.ok(tecnicoRepository.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar técnico")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tecnicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
