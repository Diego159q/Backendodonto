package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordatorioRequest {
    private Long pacienteId;

    private Long citaId;

    @NotBlank
    private String tipo;

    @NotBlank
    private String mensaje;

    private LocalDateTime fechaProgramada;

    private String medio;
}
