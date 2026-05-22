package com.noc.smartnoc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroFactura;

    @Column(nullable = false)
    private Double importe;

    @Column(length = 150)
    private String cliente;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(name = "enviado_ticketbai")
    private Boolean enviadoTicketBai = false;

    @Column(name = "firma_ticketbai", length = 255)
    private String firmaTicketBai;

    @Column(name = "qr_url", length = 255)
    private String qrUrl;

    public Factura() {}

    public Long getId() { return id; }
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public Boolean getEnviadoTicketBai() { return enviadoTicketBai; }
    public void setEnviadoTicketBai(Boolean enviadoTicketBai) { this.enviadoTicketBai = enviadoTicketBai; }
    public String getFirmaTicketBai() { return firmaTicketBai; }
    public void setFirmaTicketBai(String firmaTicketBai) { this.firmaTicketBai = firmaTicketBai; }
    public String getQrUrl() { return qrUrl; }
    public void setQrUrl(String qrUrl) { this.qrUrl = qrUrl; }
}
