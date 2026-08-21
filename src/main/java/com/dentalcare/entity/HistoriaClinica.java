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
@Table(name = "historias_clinicas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Column(name = "enfermedad_actual")
    private String enfermedadActual;

    @Column(name = "antecedentes_personales")
    private String antecedentesPersonales;

    @Column(name = "antecedentes_familiares")
    private String antecedentesFamiliares;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "enfermedades_sistemicas")
    private String enfermedadesSistemicas;

    @Column(name = "presion_arterial")
    private String presionArterial;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "talla")
    private BigDecimal talla;

    @Column(name = "temperatura")
    private BigDecimal temperatura;

    @Column(name = "diagnostico_general")
    private String diagnosticoGeneral;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "recomendaciones")
    private String recomendaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontologo_responsable_id")
    private Odontologo odontologoResponsable;

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
