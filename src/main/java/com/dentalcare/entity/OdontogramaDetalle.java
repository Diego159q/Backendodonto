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
@Table(name = "odontograma_detalles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OdontogramaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odontograma_id")
    private Odontograma odontograma;

    @Column(name = "numero_pieza")
    private Integer numeroPieza;

    @Column(name = "cara_dental")
    private String caraDental;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion")
    private CondicionDental condicion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tratamiento_pendiente")
    private Boolean tratamientoPendiente;

    @Column(name = "tratamiento_realizado")
    private Boolean tratamientoRealizado;

    @Column(name = "color")
    private String color;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
