package com.dentalcare.controller;

import com.dentalcare.dto.request.RecordatorioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.RecordatorioResponse;
import com.dentalcare.service.IRecordatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recordatorios")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
@Tag(name = "Recordatorios", description = "Gesti\u00f3n de recordatorios para pacientes")
public class RecordatorioController {

    private final IRecordatorioService recordatorioService;

    public RecordatorioController(IRecordatorioService recordatorioService) {
        this.recordatorioService = recordatorioService;
    }

    @GetMapping
    @Operation(summary = "Listar recordatorios", description = "Obtiene los recordatorios filtrados por paciente")
    public ResponseEntity<List<RecordatorioResponse>> listar(@RequestParam(required = false) Long pacienteId) {
        List<RecordatorioResponse> recordatorios;
        if (pacienteId != null) {
            recordatorios = recordatorioService.listarPorPaciente(pacienteId);
        } else {
            recordatorios = recordatorioService.listarPorPaciente(null);
        }
        return ResponseEntity.ok(recordatorios);
    }

    @PostMapping
    @Operation(summary = "Programar recordatorio", description = "Programa un nuevo recordatorio para un paciente")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody RecordatorioRequest request) {
        MensajeResponse response = recordatorioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
