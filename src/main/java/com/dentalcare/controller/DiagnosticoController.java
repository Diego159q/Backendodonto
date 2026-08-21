package com.dentalcare.controller;

import com.dentalcare.dto.request.DiagnosticoRequest;
import com.dentalcare.dto.response.DiagnosticoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IDiagnosticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnosticos")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Diagn\u00f3sticos", description = "Gesti\u00f3n de diagn\u00f3sticos odontol\u00f3gicos")
public class DiagnosticoController {

    private final IDiagnosticoService diagnosticoService;

    public DiagnosticoController(IDiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping
    @Operation(summary = "Listar diagn\u00f3sticos", description = "Obtiene todos los diagn\u00f3sticos registrados en el cat\u00e1logo")
    public ResponseEntity<List<DiagnosticoResponse>> listar() {
        List<DiagnosticoResponse> diagnosticos = diagnosticoService.listar();
        return ResponseEntity.ok(diagnosticos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de un diagn\u00f3stico")
    public ResponseEntity<DiagnosticoResponse> obtenerPorId(@PathVariable Long id) {
        DiagnosticoResponse diagnostico = diagnosticoService.obtenerPorId(id);
        return ResponseEntity.ok(diagnostico);
    }

    @PostMapping
    @Operation(summary = "Crear diagn\u00f3stico", description = "Registra un nuevo diagn\u00f3stico en el cat\u00e1logo")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody DiagnosticoRequest request) {
        MensajeResponse response = diagnosticoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar diagn\u00f3stico", description = "Actualiza un diagn\u00f3stico existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody DiagnosticoRequest request) {
        MensajeResponse response = diagnosticoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

}
