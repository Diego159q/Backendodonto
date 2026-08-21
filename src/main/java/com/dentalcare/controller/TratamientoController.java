package com.dentalcare.controller;

import com.dentalcare.dto.request.PacienteTratamientoRequest;
import com.dentalcare.dto.request.TratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteTratamientoResponse;
import com.dentalcare.dto.response.TratamientoResponse;
import com.dentalcare.service.IPacienteTratamientoService;
import com.dentalcare.service.ITratamientoService;
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

import java.util.List;

@RestController
@RequestMapping("/tratamientos")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Tratamientos", description = "Gesti\u00f3n de tratamientos odontol\u00f3gicos")
public class TratamientoController {

    private final ITratamientoService tratamientoService;
    private final IPacienteTratamientoService pacienteTratamientoService;

    public TratamientoController(ITratamientoService tratamientoService,
                                  IPacienteTratamientoService pacienteTratamientoService) {
        this.tratamientoService = tratamientoService;
        this.pacienteTratamientoService = pacienteTratamientoService;
    }

    @GetMapping("/catalogo")
    @Operation(summary = "Listar cat\u00e1logo", description = "Obtiene todos los tratamientos del cat\u00e1logo")
    public ResponseEntity<List<TratamientoResponse>> listarCatalogo() {
        List<TratamientoResponse> tratamientos = tratamientoService.listar();
        return ResponseEntity.ok(tratamientos);
    }

    @GetMapping("/catalogo/{id}")
    @Operation(summary = "Buscar en cat\u00e1logo por ID", description = "Obtiene los detalles de un tratamiento del cat\u00e1logo")
    public ResponseEntity<TratamientoResponse> obtenerDelCatalogo(@PathVariable Long id) {
        TratamientoResponse tratamiento = tratamientoService.obtenerPorId(id);
        return ResponseEntity.ok(tratamiento);
    }

    @PostMapping("/catalogo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear tratamiento en cat\u00e1logo", description = "Registra un nuevo tratamiento en el cat\u00e1logo (solo ADMIN)")
    public ResponseEntity<MensajeResponse> crearEnCatalogo(@Valid @RequestBody TratamientoRequest request) {
        MensajeResponse response = tratamientoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/catalogo/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar tratamiento en cat\u00e1logo", description = "Actualiza un tratamiento del cat\u00e1logo (solo ADMIN)")
    public ResponseEntity<MensajeResponse> actualizarEnCatalogo(@PathVariable Long id, @Valid @RequestBody TratamientoRequest request) {
        MensajeResponse response = tratamientoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar tratamientos de pacientes", description = "Lista paginada de tratamientos asignados a pacientes con filtros")
    public ResponseEntity<Page<PacienteTratamientoResponse>> listar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) String estado,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<PacienteTratamientoResponse> tratamientos = pacienteTratamientoService.listar(pacienteId, estado, pageable);
        return ResponseEntity.ok(tratamientos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tratamiento de paciente por ID", description = "Obtiene los detalles de un tratamiento asignado")
    public ResponseEntity<PacienteTratamientoResponse> obtenerPorId(@PathVariable Long id) {
        PacienteTratamientoResponse tratamiento = pacienteTratamientoService.obtenerPorId(id);
        return ResponseEntity.ok(tratamiento);
    }

    @PostMapping
    @Operation(summary = "Asignar tratamiento a paciente", description = "Asigna un tratamiento a un paciente")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody PacienteTratamientoRequest request) {
        MensajeResponse response = pacienteTratamientoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tratamiento de paciente", description = "Actualiza un tratamiento asignado a un paciente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteTratamientoRequest request) {
        MensajeResponse response = pacienteTratamientoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado", description = "Actualiza el estado de un tratamiento de paciente")
    public ResponseEntity<MensajeResponse> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        MensajeResponse response = pacienteTratamientoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/sesion")
    @Operation(summary = "Registrar sesi\u00f3n", description = "Registra una sesi\u00f3n realizada para el tratamiento")
    public ResponseEntity<MensajeResponse> registrarSesion(@PathVariable Long id) {
        MensajeResponse response = pacienteTratamientoService.registrarSesion(id);
        return ResponseEntity.ok(response);
    }
}
