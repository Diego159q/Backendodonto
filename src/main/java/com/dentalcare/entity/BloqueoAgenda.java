package com.dentalcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bloqueos_agenda")
public class BloqueoAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontologo_id")
    private Odontologo odontologo; // Si es null, aplica a toda la clínica

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio; // Si es null, bloquea todo el día

    @Column(name = "hora_fin")
    private LocalTime horaFin; // Si es null, bloquea todo el día

    @Column(name = "motivo")
    private String motivo;
}
