package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BloqueoAgendaRequest {
    private Long odontologoId; // Null = Clínica global

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private LocalTime horaInicio; // Null = Todo el día
    private LocalTime horaFin; // Null = Todo el día

    private String motivo;
}
