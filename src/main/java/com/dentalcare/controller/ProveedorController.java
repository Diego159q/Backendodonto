package com.dentalcare.controller;

import com.dentalcare.dto.request.ProveedorRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.ProveedorResponse;
import com.dentalcare.service.IProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Proveedores", description = "Gesti\u00f3n de proveedores (solo ADMIN)")
public class ProveedorController {

    private final IProveedorService proveedorService;

    public ProveedorController(IProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    @Operation(summary = "Listar proveedores", description = "Obtiene todos los proveedores registrados")
    public ResponseEntity<List<ProveedorResponse>> listar() {
        List<ProveedorResponse> proveedores = proveedorService.listar();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de un proveedor")
    public ResponseEntity<ProveedorResponse> obtenerPorId(@PathVariable Long id) {
        ProveedorResponse proveedor = proveedorService.obtenerPorId(id);
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody ProveedorRequest request) {
        MensajeResponse response = proveedorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor", description = "Actualiza un proveedor existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        MensajeResponse response = proveedorService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
}
