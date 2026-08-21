package com.dentalcare.controller;

import com.dentalcare.dto.request.HorarioAtencionRequest;
import com.dentalcare.dto.response.HorarioAtencionResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IHorarioAtencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios-atencion")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
@Tag(name = "Horarios de Atención", description = "Gestión administrativa de los horarios de atención de la clínica")
public class HorarioAtencionController {

    private final IHorarioAtencionService horarioAtencionService;

    public HorarioAtencionController(IHorarioAtencionService horarioAtencionService) {
        this.horarioAtencionService = horarioAtencionService;
    }

    @GetMapping
    @Operation(summary = "Listar horarios", description = "Obtiene todos los horarios de atención configurados")
    public ResponseEntity<List<HorarioAtencionResponse>> listarTodos() {
        return ResponseEntity.ok(horarioAtencionService.listarTodos());
    }

    @GetMapping("/odontologo/{odontologoId}")
    @Operation(summary = "Listar horarios por odontólogo", description = "Obtiene los horarios de atención de un odontólogo específico")
    public ResponseEntity<List<HorarioAtencionResponse>> listarPorOdontologo(@PathVariable Long odontologoId) {
        return ResponseEntity.ok(horarioAtencionService.listarPorOdontologo(odontologoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario por ID", description = "Obtiene el detalle de un horario específico")
    public ResponseEntity<HorarioAtencionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioAtencionService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear horario", description = "Crea un nuevo bloque de horario de atención")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody HorarioAtencionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioAtencionService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario", description = "Actualiza un horario de atención existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody HorarioAtencionRequest request) {
        return ResponseEntity.ok(horarioAtencionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar horario", description = "Elimina un horario de atención")
    public ResponseEntity<MensajeResponse> eliminar(@PathVariable Long id) {
        return ResponseEntity.ok(horarioAtencionService.eliminar(id));
    }
}
