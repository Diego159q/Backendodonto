package com.dentalcare.controller;

import com.dentalcare.dto.request.RecetaRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.RecetaResponse;
import com.dentalcare.service.IRecetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recetas")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Recetas", description = "Gesti\u00f3n de recetas m\u00e9dicas")
public class RecetaController {

    private final IRecetaService recetaService;

    public RecetaController(IRecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping
    @Operation(summary = "Listar recetas", description = "Lista recetas con filtro opcional por paciente")
    public ResponseEntity<List<RecetaResponse>> listar(@RequestParam(required = false) Long pacienteId) {
        List<RecetaResponse> recetas = recetaService.listar(pacienteId);
        return ResponseEntity.ok(recetas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de una receta")
    public ResponseEntity<RecetaResponse> obtenerPorId(@PathVariable Long id) {
        RecetaResponse receta = recetaService.obtenerPorId(id);
        return ResponseEntity.ok(receta);
    }

    @PostMapping
    @Operation(summary = "Crear receta", description = "Registra una nueva receta m\u00e9dica")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody RecetaRequest request) {
        MensajeResponse response = recetaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar receta", description = "Aprueba una receta m\u00e9dica")
    public ResponseEntity<MensajeResponse> aprobar(@PathVariable Long id) {
        MensajeResponse response = recetaService.aprobar(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anular receta", description = "Anula una receta m\u00e9dica")
    public ResponseEntity<MensajeResponse> anular(@PathVariable Long id) {
        MensajeResponse response = recetaService.anular(id);
        return ResponseEntity.ok(response);
    }
}
