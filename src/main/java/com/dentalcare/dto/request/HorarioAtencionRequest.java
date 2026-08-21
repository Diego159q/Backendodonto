package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class HorarioAtencionRequest {
    @NotNull(message = "El odontólogo es obligatorio")
    private Long odontologoId;

    @NotNull(message = "El día de la semana es obligatorio")
    private DayOfWeek diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Boolean activo = true;
}
