package com.noc.smartnoc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEquipo tipo;

    @Column(length = 100)
    private String modelo;

    @Column(name = "direccion_ip", length = 45)
    private String direccionIp;

    @Column(name = "site_id", length = 50)
    private String siteId;

    @Column(length = 100)
    private String zona;

    @Enumerated(EnumType.STRING)
    private EstadoEquipo estado = EstadoEquipo.OPERATIVO;

    private Double latitud;

    private Double longitud;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();

    // ── Enums internos ─────────────────────────────────────────────
    public enum TipoEquipo { OLT, BTS_5G, ONU, ROUTER, SWITCH, RRU }
    public enum EstadoEquipo { OPERATIVO, DEGRADADO, FALLO, MANTENIMIENTO }

    // ── Constructores ──────────────────────────────────────────────
    public Equipo() {}

    // ── Getters / Setters ──────────────────────────────────────────
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoEquipo getTipo() { return tipo; }
    public void setTipo(TipoEquipo tipo) { this.tipo = tipo; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getDireccionIp() { return direccionIp; }
    public void setDireccionIp(String direccionIp) { this.direccionIp = direccionIp; }
    public String getSiteId() { return siteId; }
    public void setSiteId(String siteId) { this.siteId = siteId; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public EstadoEquipo getEstado() { return estado; }
    public void setEstado(EstadoEquipo estado) { this.estado = estado; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
