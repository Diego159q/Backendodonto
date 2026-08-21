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
@Table(name = "auditoria")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "accion")
    private String accion;

    @Column(name = "entidad")
    private String entidad;

    @Column(name = "entidad_id")
    private Long entidadId;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "direccion_ip")
    private String direccionIp;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "datos_anteriores", columnDefinition = "TEXT")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "TEXT")
    private String datosNuevos;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}
