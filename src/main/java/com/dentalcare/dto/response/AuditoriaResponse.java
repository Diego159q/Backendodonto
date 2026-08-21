package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaResponse {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String accion;
    private String entidad;
    private Long entidadId;
    private String descripcion;
    private String direccionIp;
    private LocalDateTime fecha;
    private String datosAnteriores;
    private String datosNuevos;
}
