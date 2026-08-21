package com.dentalcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recordatorios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRecordatorio tipo;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "enviado")
    private Boolean enviado;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio")
    private MedioRecordatorio medio;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
