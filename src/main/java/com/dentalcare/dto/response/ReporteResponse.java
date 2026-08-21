package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponse {
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Map<String, Object>> datos;
}
