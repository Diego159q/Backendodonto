package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvolucionClinicaRequest {
    @NotNull
    private Long historiaClinicaId;

    private Long citaId;

    @NotNull
    private Long odontologoId;

    private LocalDate fecha;

    @NotBlank
    private String descripcion;

    private String procedimientoRealizado;

    private String observaciones;

    private String recomendaciones;
}
