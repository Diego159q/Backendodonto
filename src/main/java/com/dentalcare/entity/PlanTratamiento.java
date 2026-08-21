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
@Table(name = "planes_tratamiento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlanTratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontologo_id")
    private Odontologo odontologo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "monto_total")
    private BigDecimal montoTotal;

    @Column(name = "descuento_total")
    private BigDecimal descuentoTotal;

    @Column(name = "monto_final")
    private BigDecimal montoFinal;

    @Column(name = "adelanto")
    private BigDecimal adelanto;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "aceptado_por_paciente")
    private Boolean aceptadoPorPaciente;

    @Column(name = "fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

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
