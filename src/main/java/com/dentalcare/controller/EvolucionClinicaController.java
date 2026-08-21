package com.dentalcare.controller;

import com.dentalcare.dto.request.EvolucionClinicaRequest;
import com.dentalcare.dto.response.EvolucionClinicaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IEvolucionClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evoluciones")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Evoluci\u00f3n Cl\u00ednica", description = "Gesti\u00f3n de evoluci\u00f3n cl\u00ednica de pacientes")
public class EvolucionClinicaController {

    private final IEvolucionClinicaService evolucionClinicaService;

    public EvolucionClinicaController(IEvolucionClinicaService evolucionClinicaService) {
        this.evolucionClinicaService = evolucionClinicaService;
    }

    @GetMapping("/historia/{historiaClinicaId}")
    @Operation(summary = "Listar por historia cl\u00ednica", description = "Obtiene todas las evoluciones de una historia cl\u00ednica")
    public ResponseEntity<List<EvolucionClinicaResponse>> listarPorHistoriaClinica(@PathVariable Long historiaClinicaId) {
        List<EvolucionClinicaResponse> evoluciones = evolucionClinicaService.listarPorHistoriaClinica(historiaClinicaId);
        return ResponseEntity.ok(evoluciones);
    }

    @PostMapping
    @Operation(summary = "Registrar evoluci\u00f3n", description = "Registra una nueva evoluci\u00f3n cl\u00ednica")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody EvolucionClinicaRequest request) {
        MensajeResponse response = evolucionClinicaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
