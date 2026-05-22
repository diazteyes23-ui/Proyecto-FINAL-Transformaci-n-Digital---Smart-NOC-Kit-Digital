package com.noc.smartnoc.controller;

import com.noc.smartnoc.model.Equipo;
import com.noc.smartnoc.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/equipos")
@CrossOrigin(origins = "*") // Note: Security is added so this is okay for the demo
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @GetMapping
    public List<Equipo> getAll() {
        return equipoService.findAll();
    }

    @GetMapping("/{id}")
    public Equipo getById(@PathVariable Long id) {
        return equipoService.findById(id);
    }

    @GetMapping("/estado/{estado}")
    public List<Equipo> getByEstado(@PathVariable Equipo.EstadoEquipo estado) {
        return equipoService.findByEstado(estado);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Equipo> getByTipo(@PathVariable Equipo.TipoEquipo tipo) {
        return equipoService.findByTipo(tipo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipo create(@Valid @RequestBody Equipo equipo) {
        return equipoService.create(equipo);
    }

    @PatchMapping("/{id}/estado")
    public Equipo updateEstado(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Equipo.EstadoEquipo estado = Equipo.EstadoEquipo.valueOf(payload.get("estado"));
        return equipoService.updateEstado(id, estado);
    }
}
