package com.dentalcare.controller;

import com.dentalcare.dto.request.CuotaRequest;
import com.dentalcare.dto.response.CuotaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.ICuotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuotas")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
@Tag(name = "Cuotas", description = "Gesti\u00f3n de cuotas de planes de tratamiento")
public class CuotaController {

    private final ICuotaService cuotaService;

    public CuotaController(ICuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    @GetMapping("/plan/{planId}")
    @Operation(summary = "Listar cuotas por plan", description = "Obtiene todas las cuotas de un plan de tratamiento")
    public ResponseEntity<List<CuotaResponse>> listarPorPlan(@PathVariable Long planId) {
        List<CuotaResponse> cuotas = cuotaService.listarPorPlan(planId);
        return ResponseEntity.ok(cuotas);
    }

    @PostMapping
    @Operation(summary = "Crear cuota", description = "Registra una nueva cuota para un plan de tratamiento")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody CuotaRequest request) {
        MensajeResponse response = cuotaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/pagar")
    @Operation(summary = "Pagar cuota", description = "Marca una cuota como pagada asoci\u00e1ndola a un pago")
    public ResponseEntity<MensajeResponse> pagar(@PathVariable Long id, @RequestParam Long pagoId) {
        MensajeResponse response = cuotaService.pagarCuota(id, pagoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vencidas")
    @Operation(summary = "Cuotas vencidas", description = "Obtiene las cuotas con fecha de vencimiento pasada y pendientes")
    public ResponseEntity<List<CuotaResponse>> listarVencidas() {
        List<CuotaResponse> cuotas = cuotaService.listarCuotasVencidas();
        return ResponseEntity.ok(cuotas);
    }
}
