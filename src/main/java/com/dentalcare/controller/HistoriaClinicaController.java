package com.dentalcare.controller;

import com.dentalcare.dto.request.HistoriaClinicaRequest;
import com.dentalcare.dto.response.HistoriaClinicaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IHistoriaClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historias-clinicas")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Historias Cl\u00ednicas", description = "Gesti\u00f3n de historias cl\u00ednicas de pacientes")
public class HistoriaClinicaController {

    private final IHistoriaClinicaService historiaClinicaService;

    public HistoriaClinicaController(IHistoriaClinicaService historiaClinicaService) {
        this.historiaClinicaService = historiaClinicaService;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar por paciente", description = "Obtiene todas las historias cl\u00ednicas de un paciente")
    public ResponseEntity<List<HistoriaClinicaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<HistoriaClinicaResponse> historias = historiaClinicaService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(historias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de una historia cl\u00ednica")
    public ResponseEntity<HistoriaClinicaResponse> obtenerPorId(@PathVariable Long id) {
        HistoriaClinicaResponse historia = historiaClinicaService.obtenerPorId(id);
        return ResponseEntity.ok(historia);
    }

    @PostMapping
    @Operation(summary = "Crear historia cl\u00ednica", description = "Registra una nueva historia cl\u00ednica")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody HistoriaClinicaRequest request) {
        MensajeResponse response = historiaClinicaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar historia cl\u00ednica", description = "Actualiza una historia cl\u00ednica existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody HistoriaClinicaRequest request) {
        MensajeResponse response = historiaClinicaService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
}
