package com.noc.smartnoc.controller;

import com.noc.smartnoc.model.Factura;
import com.noc.smartnoc.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public List<Factura> getAll() {
        return facturaService.findAll();
    }

    @GetMapping("/{id}")
    public Factura getById(@PathVariable Long id) {
        return facturaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Factura create(@Valid @RequestBody Factura factura) {
        return facturaService.create(factura);
    }

    @PostMapping("/{id}/ticketbai")
    public Factura enviarTicketBai(@PathVariable Long id) {
        return facturaService.enviarATicketBai(id);
    }
}
