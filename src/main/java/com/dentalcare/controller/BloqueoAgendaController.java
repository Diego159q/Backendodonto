package com.dentalcare.controller;

import com.dentalcare.dto.request.BloqueoAgendaRequest;
import com.dentalcare.dto.response.BloqueoAgendaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IBloqueoAgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bloqueos-agenda")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
@Tag(name = "Bloqueos de Agenda", description = "Gestión administrativa de bloqueos de fechas y horarios")
public class BloqueoAgendaController {

    private final IBloqueoAgendaService bloqueoAgendaService;

    public BloqueoAgendaController(IBloqueoAgendaService bloqueoAgendaService) {
        this.bloqueoAgendaService = bloqueoAgendaService;
    }

    @GetMapping
    @Operation(summary = "Listar bloqueos", description = "Obtiene todos los bloqueos de agenda configurados")
    public ResponseEntity<List<BloqueoAgendaResponse>> listarTodos() {
        return ResponseEntity.ok(bloqueoAgendaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bloqueo por ID", description = "Obtiene el detalle de un bloqueo específico")
    public ResponseEntity<BloqueoAgendaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bloqueoAgendaService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear bloqueo", description = "Crea un nuevo bloqueo de agenda")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody BloqueoAgendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloqueoAgendaService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bloqueo", description = "Actualiza un bloqueo de agenda existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody BloqueoAgendaRequest request) {
        return ResponseEntity.ok(bloqueoAgendaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bloqueo", description = "Elimina un bloqueo de agenda")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        return ResponseEntity.ok(bloqueoAgendaService.eliminar(id));
    }
}
