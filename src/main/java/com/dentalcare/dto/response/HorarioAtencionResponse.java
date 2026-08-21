package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioAtencionResponse {
    private Long id;
    private Long odontologoId;
    private String odontologoNombre;
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean activo;
}
