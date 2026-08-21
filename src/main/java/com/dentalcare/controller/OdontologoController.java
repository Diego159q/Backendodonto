package com.dentalcare.controller;

import com.dentalcare.dto.request.OdontologoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.OdontologoResponse;
import com.dentalcare.service.IOdontologoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
@Tag(name = "Odont\u00f3logos", description = "Gesti\u00f3n de odont\u00f3logos")
public class OdontologoController {

    private final IOdontologoService odontologoService;

    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
    @Operation(summary = "Listar odont\u00f3logos", description = "Obtiene todos los odont\u00f3logos registrados")
    public ResponseEntity<List<OdontologoResponse>> listar() {
        List<OdontologoResponse> odontologos = odontologoService.listar();
        return ResponseEntity.ok(odontologos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA', 'RECEPCIONISTA')")
    @Operation(summary = "Buscar odont\u00f3logo por ID", description = "Obtiene los detalles de un odont\u00f3logo espec\u00edfico")
    public ResponseEntity<OdontologoResponse> obtenerPorId(@PathVariable Long id) {
        OdontologoResponse odontologo = odontologoService.obtenerPorId(id);
        return ResponseEntity.ok(odontologo);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear odont\u00f3logo", description = "Registra un nuevo odont\u00f3logo (solo ADMIN)")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody OdontologoRequest request) {
        MensajeResponse response = odontologoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar odont\u00f3logo", description = "Actualiza los datos de un odont\u00f3logo existente (solo ADMIN)")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody OdontologoRequest request) {
        MensajeResponse response = odontologoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Cambiar estado", description = "Activa o desactiva un odont\u00f3logo (solo ADMIN)")
    public ResponseEntity<MensajeResponse> cambiarEstado(@PathVariable Long id) {
        MensajeResponse response = odontologoService.cambiarEstado(id);
        return ResponseEntity.ok(response);
    }
}
