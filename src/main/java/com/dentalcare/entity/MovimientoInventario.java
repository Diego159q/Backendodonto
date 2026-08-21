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
@Table(name = "movimientos_inventario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento")
    private TipoMovimiento tipoMovimiento;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "stock_anterior")
    private Integer stockAnterior;

    @Column(name = "stock_nuevo")
    private Integer stockNuevo;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_registro_id")
    private Usuario usuarioRegistro;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}
