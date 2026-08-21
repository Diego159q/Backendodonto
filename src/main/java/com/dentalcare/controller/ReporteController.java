package com.dentalcare.controller;

import com.dentalcare.dto.response.DashboardResponse;
import com.dentalcare.dto.response.ReporteResponse;
import com.dentalcare.service.IDashboardService;
import com.dentalcare.service.IReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Generaci\u00f3n y exportaci\u00f3n de reportes")
public class ReporteController {

    private final IReporteService reporteService;
    private final IDashboardService dashboardService;

    public ReporteController(IReporteService reporteService, IDashboardService dashboardService) {
        this.reporteService = reporteService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
    @Operation(summary = "Obtener dashboard", description = "Obtiene los indicadores principales del sistema")
    public ResponseEntity<DashboardResponse> obtenerDashboard() {
        DashboardResponse dashboard = dashboardService.obtenerDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/{tipo}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Generar reporte", description = "Genera un reporte del tipo especificado con filtros de fecha")
    public ResponseEntity<ReporteResponse> generarReporte(
            @PathVariable String tipo,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {
        ReporteResponse reporte = reporteService.generarReporte(tipo, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/{tipo}/pdf")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Exportar PDF", description = "Exporta un reporte a formato PDF")
    public ResponseEntity<Resource> exportarPdf(
            @PathVariable String tipo,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {
        byte[] pdfBytes = reporteService.exportarPdf(tipo, fechaInicio, fechaFin);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-" + tipo + ".pdf")
                .body(resource);
    }

    @GetMapping("/{tipo}/excel")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Exportar Excel", description = "Exporta un reporte a formato Excel")
    public ResponseEntity<Resource> exportarExcel(
            @PathVariable String tipo,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {
        byte[] excelBytes = reporteService.exportarExcel(tipo, fechaInicio, fechaFin);
        ByteArrayResource resource = new ByteArrayResource(excelBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-" + tipo + ".xlsx")
                .body(resource);
    }

    @GetMapping("/{tipo}/csv")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Exportar CSV", description = "Exporta un reporte a formato CSV")
    public ResponseEntity<Resource> exportarCsv(
            @PathVariable String tipo,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {
        byte[] csvBytes = reporteService.exportarCsv(tipo, fechaInicio, fechaFin);
        ByteArrayResource resource = new ByteArrayResource(csvBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-" + tipo + ".csv")
                .body(resource);
    }
}
