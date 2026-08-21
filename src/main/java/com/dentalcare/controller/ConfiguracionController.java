package com.dentalcare.controller;

import com.dentalcare.dto.request.ConfiguracionRequest;
import com.dentalcare.dto.response.ConfiguracionResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IConfiguracionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuracion")
@Tag(name = "Configuraci\u00f3n", description = "Configuraci\u00f3n general del sistema")
public class ConfiguracionController {

    private final IConfiguracionService configuracionService;

    public ConfiguracionController(IConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @GetMapping
    @Operation(summary = "Obtener configuraci\u00f3n", description = "Obtiene la configuraci\u00f3n actual del sistema (p\u00fablico)")
    public ResponseEntity<ConfiguracionResponse> obtenerConfiguracion() {
        ConfiguracionResponse config = configuracionService.obtenerConfiguracion();
        return ResponseEntity.ok(config);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar configuraci\u00f3n", description = "Actualiza la configuraci\u00f3n del sistema (solo ADMIN)")
    public ResponseEntity<MensajeResponse> actualizar(@Valid @RequestBody ConfiguracionRequest request) {
        MensajeResponse response = configuracionService.actualizar(request);
        return ResponseEntity.ok(response);
    }
}
