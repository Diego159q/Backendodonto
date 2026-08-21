package com.dentalcare.controller;

import com.dentalcare.dto.request.OdontogramaDetalleRequest;
import com.dentalcare.dto.request.OdontogramaRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.OdontogramaResponse;
import com.dentalcare.service.IOdontogramaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontogramas")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Odontogramas", description = "Gesti\u00f3n de odontogramas de pacientes")
public class OdontogramaController {

    private final IOdontogramaService odontogramaService;

    public OdontogramaController(IOdontogramaService odontogramaService) {
        this.odontogramaService = odontogramaService;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Odontograma actual del paciente", description = "Obtiene el odontograma m\u00e1s reciente de un paciente")
    public ResponseEntity<OdontogramaResponse> obtenerActualPorPaciente(@PathVariable Long pacienteId) {
        OdontogramaResponse odontograma = odontogramaService.obtenerActualPorPaciente(pacienteId);
        return ResponseEntity.ok(odontograma);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de un odontograma")
    public ResponseEntity<OdontogramaResponse> obtenerPorId(@PathVariable Long id) {
        OdontogramaResponse odontograma = odontogramaService.obtenerPorId(id);
        return ResponseEntity.ok(odontograma);
    }

    @PostMapping
    @Operation(summary = "Crear odontograma", description = "Registra un nuevo odontograma")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody OdontogramaRequest request) {
        MensajeResponse response = odontogramaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/detalles")
    @Operation(summary = "Agregar detalle", description = "Agrega un detalle individual al odontograma")
    public ResponseEntity<MensajeResponse> agregarDetalle(
            @PathVariable Long id,
            @Valid @RequestBody OdontogramaDetalleRequest request) {
        MensajeResponse response = odontogramaService.agregarDetalle(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/detalles")
    @Operation(summary = "Actualizar detalles", description = "Actualiza todos los detalles del odontograma")
    public ResponseEntity<MensajeResponse> actualizarDetalles(
            @PathVariable Long id,
            @Valid @RequestBody List<OdontogramaDetalleRequest> detalles) {
        MensajeResponse response = odontogramaService.actualizarDetalles(id, detalles);
        return ResponseEntity.ok(response);
    }
}
