package com.dentalcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paciente_tratamientos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PacienteTratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostico_id")
    private Diagnostico diagnostico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontologo_id")
    private Odontologo odontologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tratamiento_id")
    private Tratamiento tratamiento;

    @Column(name = "pieza_dental")
    private Integer piezaDental;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Column(name = "precio")
    private BigDecimal precio;

    @Column(name = "descuento")
    private BigDecimal descuento;

    @Column(name = "precio_final")
    private BigDecimal precioFinal;

    @Column(name = "numero_sesiones")
    private Integer numeroSesiones;

    @Column(name = "sesiones_realizadas")
    private Integer sesionesRealizadas;

    @Column(name = "porcentaje_avance")
    private Integer porcentajeAvance;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTratamiento estado;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
