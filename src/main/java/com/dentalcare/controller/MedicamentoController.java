package com.dentalcare.controller;

import com.dentalcare.dto.request.MedicamentoRequest;
import com.dentalcare.dto.response.MedicamentoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IMedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicamentos")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Medicamentos", description = "Gesti\u00f3n de medicamentos")
public class MedicamentoController {

    private final IMedicamentoService medicamentoService;

    public MedicamentoController(IMedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    @Operation(summary = "Listar medicamentos", description = "Obtiene todos los medicamentos registrados")
    public ResponseEntity<List<MedicamentoResponse>> listar() {
        List<MedicamentoResponse> medicamentos = medicamentoService.listar();
        return ResponseEntity.ok(medicamentos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear medicamento", description = "Registra un nuevo medicamento (solo ADMIN)")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody MedicamentoRequest request) {
        MensajeResponse response = medicamentoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar medicamento", description = "Actualiza un medicamento existente (solo ADMIN)")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody MedicamentoRequest request) {
        MensajeResponse response = medicamentoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
}
