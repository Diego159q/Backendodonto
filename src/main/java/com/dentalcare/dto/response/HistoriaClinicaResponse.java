package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private LocalDate fechaApertura;
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
    private Long odontologoId;
    private String odontologoNombre;
    private LocalDateTime fechaCreacion;
}
