package com.dentalcare.controller;

import com.dentalcare.dto.request.PlanTratamientoDetalleRequest;
import com.dentalcare.dto.request.PlanTratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PlanTratamientoResponse;
import com.dentalcare.service.IPlanTratamientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planes-tratamiento")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Planes de Tratamiento", description = "Gesti\u00f3n de planes de tratamiento")
public class PlanTratamientoController {

    private final IPlanTratamientoService planTratamientoService;

    public PlanTratamientoController(IPlanTratamientoService planTratamientoService) {
        this.planTratamientoService = planTratamientoService;
    }

    @GetMapping
    @Operation(summary = "Listar planes", description = "Obtiene todos los planes de tratamiento o filtra por paciente")
    public ResponseEntity<List<PlanTratamientoResponse>> listar(@RequestParam(required = false) Long pacienteId) {
        List<PlanTratamientoResponse> planes = pacienteId != null
                ? planTratamientoService.listarPorPaciente(pacienteId)
                : planTratamientoService.listarTodos();
        return ResponseEntity.ok(planes);
    }
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar por paciente", description = "Obtiene los planes de tratamiento de un paciente")
    public ResponseEntity<List<PlanTratamientoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<PlanTratamientoResponse> planes = planTratamientoService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(planes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de un plan de tratamiento")
    public ResponseEntity<PlanTratamientoResponse> obtenerPorId(@PathVariable Long id) {
        PlanTratamientoResponse plan = planTratamientoService.obtenerPorId(id);
        return ResponseEntity.ok(plan);
    }

    @PostMapping
    @Operation(summary = "Crear plan de tratamiento", description = "Registra un nuevo plan de tratamiento con sus detalles")
    public ResponseEntity<MensajeResponse> crear(
            @Valid @RequestBody PlanTratamientoRequest request,
            @RequestParam(required = false) List<PlanTratamientoDetalleRequest> detalles) {
        MensajeResponse response = planTratamientoService.crear(request, detalles);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/aceptar")
    @Operation(summary = "Aceptar plan", description = "Marca un plan de tratamiento como aceptado por el paciente")
    public ResponseEntity<MensajeResponse> aceptar(@PathVariable Long id) {
        MensajeResponse response = planTratamientoService.aceptarPlan(id);
        return ResponseEntity.ok(response);
    }
}
