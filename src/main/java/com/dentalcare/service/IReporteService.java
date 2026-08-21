package com.dentalcare.service;

import com.dentalcare.dto.response.ReporteResponse;

import java.time.LocalDate;

public interface IReporteService {
    ReporteResponse generarReporte(String tipo, LocalDate fechaInicio, LocalDate fechaFin);
    byte[] exportarPdf(String tipo, LocalDate fechaInicio, LocalDate fechaFin);
    byte[] exportarExcel(String tipo, LocalDate fechaInicio, LocalDate fechaFin);
    byte[] exportarCsv(String tipo, LocalDate fechaInicio, LocalDate fechaFin);
}
