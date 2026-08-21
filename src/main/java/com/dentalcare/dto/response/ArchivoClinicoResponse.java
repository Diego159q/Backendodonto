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
public class ArchivoClinicoResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private Long historiaClinicaId;
    private String tipoArchivo;
    private String nombreArchivo;
    private String url;
    private Long tamano;
    private String descripcion;
    private LocalDateTime fechaSubida;
    private Long usuarioRegistroId;
}
