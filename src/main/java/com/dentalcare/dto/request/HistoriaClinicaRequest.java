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
public class HistoriaClinicaRequest {
    @NotNull
    private Long pacienteId;

    private LocalDate fechaApertura;

    @NotBlank
    private String motivoConsulta;

    private String enfermedadActual;

    private String antecedentesPersonales;

    private String antecedentesFamiliares;

    private String alergias;

    private String enfermedadesSistemicas;

    private String presionArterial;

    private Double peso;

    private Double talla;

    private Double temperatura;

    private String diagnosticoGeneral;

    private String observaciones;

    private String recomendaciones;

    @NotNull
    private Long odontologoResponsableId;
}
