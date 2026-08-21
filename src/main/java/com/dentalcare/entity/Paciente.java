package com.dentalcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_paciente", unique = true, nullable = false)
    private String codigoPaciente;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Transient
    private Integer edad;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "distrito")
    private String distrito;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "estado_civil")
    private String estadoCivil;

    @Column(name = "ocupacion")
    private String ocupacion;

    @Column(name = "tipo_sangre")
    private String tipoSangre;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "enfermedades_previas")
    private String enfermedadesPrevias;

    @Column(name = "medicamentos_actuales")
    private String medicamentosActuales;

    @Column(name = "contacto_emergencia")
    private String contactoEmergencia;

    @Column(name = "telefono_emergencia")
    private String telefonoEmergencia;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Integer getEdad() {
        if (fechaNacimiento != null) {
            return Period.between(fechaNacimiento, LocalDate.now()).getYears();
        }
        return null;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
