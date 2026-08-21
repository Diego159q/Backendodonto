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
@Table(name = "configuracion_centro")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConfiguracionCentro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_centro")
    private String nombreCentro;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "horario_atencion")
    private String horarioAtencion;

    @Column(name = "duracion_cita_predeterminada")
    private Integer duracionCitaPredeterminada = 30;

    @Column(name = "moneda")
    private String moneda = "PEN";

    @Column(name = "mensaje_recordatorio")
    private String mensajeRecordatorio;

    @Column(name = "nombre_odontologa")
    private String nombreOdontologa;

    @Column(name = "colegiatura")
    private String colegiatura;

    @Column(name = "firma_url")
    private String firmaUrl;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
