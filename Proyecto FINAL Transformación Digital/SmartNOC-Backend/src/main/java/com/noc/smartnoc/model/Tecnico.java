package com.noc.smartnoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tecnicos")
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String skills;

    @Column(length = 100)
    private String zona;

    @Column(nullable = false)
    private Boolean disponible = true;

    private Double latitud;

    private Double longitud;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();

    // ── Constructores ─────────────────────────────────────────────
    public Tecnico() {}

    // ── Getters / Setters ─────────────────────────────────────────
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
