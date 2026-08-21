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
@Table(name = "archivos_clinicos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArchivoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_clinica_id")
    private HistoriaClinica historiaClinica;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_archivo")
    private TipoArchivo tipoArchivo;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "url")
    private String url;

    @Column(name = "tamano")
    private Long tamano;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_registro_id")
    private Usuario usuarioRegistro;

    @PrePersist
    protected void onCreate() {
        if (fechaSubida == null) {
            fechaSubida = LocalDateTime.now();
        }
    }
}
