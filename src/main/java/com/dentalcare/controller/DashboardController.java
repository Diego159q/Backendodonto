package com.dentalcare.controller;

import com.dentalcare.dto.response.DashboardResponse;
import com.dentalcare.service.IDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
@Tag(name = "Dashboard", description = "Indicadores y resumen del sistema")
public class DashboardController {

    private final IDashboardService dashboardService;

    public DashboardController(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(summary = "Obtener dashboard", description = "Obtiene los indicadores principales del sistema")
    public ResponseEntity<DashboardResponse> getDashboard() {
        DashboardResponse dashboard = dashboardService.obtenerDashboard();
        return ResponseEntity.ok(dashboard);
    }
}
