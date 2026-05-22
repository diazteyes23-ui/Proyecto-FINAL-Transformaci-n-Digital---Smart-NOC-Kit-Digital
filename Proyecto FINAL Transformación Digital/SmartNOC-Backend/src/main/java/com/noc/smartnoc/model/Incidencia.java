package com.noc.smartnoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severidad severidad = Severidad.P3;

    private String tipo;

    @Enumerated(EnumType.STRING)
    private EstadoIncidencia estado = EstadoIncidencia.ABIERTA;

    @Column(name = "clientes_afect")
    @Min(value = 0, message = "Los clientes afectados no pueden ser negativos")
    private Integer clientesAfectados = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura = LocalDateTime.now();

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "ttd_minutos")
    private Integer ttdMinutos;

    @Column(name = "ttr_minutos")
    private Integer ttrMinutos;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();

    // ── Enums internos ────────────────────────────────────────────
    public enum Severidad { P1, P2, P3, P4 }

    public enum EstadoIncidencia { ABIERTA, EN_PROGRESO, RESUELTA, CERRADA }

    // ── Constructores ─────────────────────────────────────────────
    public Incidencia() {}

    // ── Getters / Setters ─────────────────────────────────────────
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Severidad getSeveridad() { return severidad; }
    public void setSeveridad(Severidad severidad) { this.severidad = severidad; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public EstadoIncidencia getEstado() { return estado; }
    public void setEstado(EstadoIncidencia estado) { this.estado = estado; }
    public Integer getClientesAfectados() { return clientesAfectados; }
    public void setClientesAfectados(Integer clientesAfectados) { this.clientesAfectados = clientesAfectados; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
    public Integer getTtdMinutos() { return ttdMinutos; }
    public void setTtdMinutos(Integer ttdMinutos) { this.ttdMinutos = ttdMinutos; }
    public Integer getTtrMinutos() { return ttrMinutos; }
    public void setTtrMinutos(Integer ttrMinutos) { this.ttrMinutos = ttrMinutos; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
