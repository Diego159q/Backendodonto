package com.dentalcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_tratamiento_detalles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlanTratamientoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_tratamiento_id")
    private PlanTratamiento planTratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_tratamiento_id")
    private PacienteTratamiento tratamiento;

    @Column(name = "pieza_dental")
    private Integer piezaDental;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "descuento")
    private BigDecimal descuento;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "estado")
    private String estado;

    @Column(name = "numero_sesiones")
    private Integer numeroSesiones;
}
