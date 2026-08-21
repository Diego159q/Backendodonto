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
public class OdontologoResponse {
    private Long id;
    private Long usuarioId;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private String especialidad;
    private String numeroColegiatura;
    private String horarioAtencion;
    private String firmaUrl;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
