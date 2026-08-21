package com.dentalcare.controller;

import com.dentalcare.dto.request.PacienteRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteResponse;
import com.dentalcare.service.IPacienteService;
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

@RestController
@RequestMapping("/pacientes")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
@Tag(name = "Pacientes", description = "Gesti\u00f3n de pacientes")
public class PacienteController {

    private final IPacienteService pacienteService;

    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @Operation(summary = "Listar pacientes", description = "Lista paginada de pacientes con b\u00fasqueda opcional")
    public ResponseEntity<Page<PacienteResponse>> listar(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<PacienteResponse> pacientes = pacienteService.listar(search, pageable);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Obtiene los detalles de un paciente")
    public ResponseEntity<PacienteResponse> obtenerPorId(@PathVariable Long id) {
        PacienteResponse paciente = pacienteService.obtenerPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/dni/{dni}")
    @Operation(summary = "Buscar paciente por DNI", description = "Obtiene un paciente por su n\u00famero de documento")
    public ResponseEntity<PacienteResponse> obtenerPorDni(@PathVariable String dni) {
        PacienteResponse paciente = pacienteService.obtenerPorDni(dni);
        return ResponseEntity.ok(paciente);
    }

    @PostMapping
    @Operation(summary = "Crear paciente", description = "Registra un nuevo paciente")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody PacienteRequest request) {
        MensajeResponse response = pacienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequest request) {
        MensajeResponse response = pacienteService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar paciente", description = "Elimina (desactiva) un paciente (solo ADMIN)")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        MensajeResponse response = pacienteService.eliminar(id);
        return ResponseEntity.ok(response);
    }
}
