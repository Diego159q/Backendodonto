package com.dentalcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evoluciones_clinicas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EvolucionClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_clinica_id")
    private HistoriaClinica historiaClinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontologo_id")
    private Odontologo odontologo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "procedimiento_realizado")
    private String procedimientoRealizado;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "recomendaciones")
    private String recomendaciones;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
