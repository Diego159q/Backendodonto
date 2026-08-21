package com.dentalcare.controller;

import com.dentalcare.dto.request.CancelarCitaRequest;
import com.dentalcare.dto.request.CitaRequest;
import com.dentalcare.dto.request.ReprogramarCitaRequest;
import com.dentalcare.dto.response.CitaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.ICitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/citas")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
@Tag(name = "Citas", description = "Gesti\u00f3n de citas odontol\u00f3gicas")
public class CitaController {

    private final ICitaService citaService;

    public CitaController(ICitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    @Operation(summary = "Listar citas", description = "Lista paginada de citas con filtros opcionales")
    public ResponseEntity<Page<CitaResponse>> listar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long odontologoId,
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) String estado,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CitaResponse> citas = citaService.listar(pacienteId, odontologoId, fecha, estado, pageable);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cita por ID", description = "Obtiene los detalles de una cita")
    public ResponseEntity<CitaResponse> obtenerPorId(@PathVariable Long id) {
        CitaResponse cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    @Operation(summary = "Crear cita", description = "Agenda una nueva cita")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody CitaRequest request) {
        MensajeResponse response = citaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody CitaRequest request) {
        MensajeResponse response = citaService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar cita", description = "Cancela una cita con un motivo")
    public ResponseEntity<MensajeResponse> cancelar(@PathVariable Long id, @Valid @RequestBody CancelarCitaRequest request) {
        MensajeResponse response = citaService.cancelar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar cita", description = "Confirma una cita pendiente")
    public ResponseEntity<MensajeResponse> confirmar(@PathVariable Long id) {
        MensajeResponse response = citaService.confirmar(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reprogramar")
    @Operation(summary = "Reprogramar cita", description = "Reprograma una cita con nueva fecha y hora")
    public ResponseEntity<MensajeResponse> reprogramar(@PathVariable Long id, @Valid @RequestBody ReprogramarCitaRequest request) {
        MensajeResponse response = citaService.reprogramar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/asistio")
    @Operation(summary = "Marcar asistencia", description = "Marca una cita como atendida")
    public ResponseEntity<MensajeResponse> marcarAsistio(@PathVariable Long id) {
        MensajeResponse response = citaService.marcarAsistio(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/no-asistio")
    @Operation(summary = "Marcar inasistencia", description = "Marca una cita como no asistida")
    public ResponseEntity<MensajeResponse> marcarNoAsistio(@PathVariable Long id) {
        MensajeResponse response = citaService.marcarNoAsistio(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/horarios-disponibles")
    @Operation(summary = "Horarios disponibles", description = "Obtiene los horarios disponibles para un odont\u00f3logo en una fecha")
    public ResponseEntity<List<String>> obtenerHorariosDisponibles(
            @RequestParam Long odontologoId,
            @RequestParam LocalDate fecha) {
        List<String> horarios = citaService.obtenerHorariosDisponibles(odontologoId, fecha);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/hoy")
    @Operation(summary = "Citas del d\u00eda", description = "Obtiene las citas programadas para hoy")
    public ResponseEntity<List<CitaResponse>> obtenerCitasDelDia() {
        List<CitaResponse> citas = citaService.obtenerCitasDelDia();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/proximas")
    @Operation(summary = "Pr\u00f3ximas citas", description = "Obtiene las pr\u00f3ximas citas programadas")
    public ResponseEntity<List<CitaResponse>> obtenerProximasCitas() {
        List<CitaResponse> citas = citaService.obtenerProximasCitas();
        return ResponseEntity.ok(citas);
    }
}
