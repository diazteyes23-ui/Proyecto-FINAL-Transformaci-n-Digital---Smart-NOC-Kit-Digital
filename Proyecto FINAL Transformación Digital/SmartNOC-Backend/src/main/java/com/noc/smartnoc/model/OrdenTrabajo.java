package com.noc.smartnoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_trabajo")
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    @NotNull(message = "La incidencia asociada es obligatoria")
    private Incidencia incidencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    @Enumerated(EnumType.STRING)
    private Incidencia.Severidad prioridad = Incidencia.Severidad.P3;

    @Column(name = "sla_deadline")
    private LocalDateTime slaDeadline;

    @Column(name = "eta_minutos")
    private Integer etaMinutos;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();

    // ── Enum ──────────────────────────────────────────────────────
    public enum EstadoOrden {
        PENDIENTE, DESPACHADA, EN_CAMINO, EN_PROGRESO, COMPLETADA, CANCELADA
    }

    // ── Constructores ─────────────────────────────────────────────
    public OrdenTrabajo() {}

    // ── Getters / Setters ─────────────────────────────────────────
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Incidencia getIncidencia() { return incidencia; }
    public void setIncidencia(Incidencia incidencia) { this.incidencia = incidencia; }
    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public Incidencia.Severidad getPrioridad() { return prioridad; }
    public void setPrioridad(Incidencia.Severidad prioridad) { this.prioridad = prioridad; }
    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }
    public Integer getEtaMinutos() { return etaMinutos; }
    public void setEtaMinutos(Integer etaMinutos) { this.etaMinutos = etaMinutos; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public LocalDateTime getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDateTime fechaDespacho) { this.fechaDespacho = fechaDespacho; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
