package com.noc.smartnoc.service;

import com.noc.smartnoc.model.Factura;
import com.noc.smartnoc.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    public Factura findById(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada"));
    }

    public Factura create(Factura factura) {
        return facturaRepository.save(factura);
    }

    public Factura enviarATicketBai(Long id) {
        Factura factura = findById(id);
        
        if (factura.getEnviadoTicketBai()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La factura ya ha sido enviada a TicketBAI");
        }

        // Simulación del proceso de firma y envío a TicketBAI
        String signature = "TBAI-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        String qrUrl = "https://ticketbai.eus/qr?id=" + signature;

        factura.setEnviadoTicketBai(true);
        factura.setFirmaTicketBai(signature);
        factura.setQrUrl(qrUrl);

        return facturaRepository.save(factura);
    }
}
