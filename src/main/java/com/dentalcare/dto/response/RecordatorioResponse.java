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
public class RecordatorioResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long citaId;
    private String tipo;
    private String mensaje;
    private LocalDateTime fechaProgramada;
    private Boolean enviado;
    private String medio;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaCreacion;
}
