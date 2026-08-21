package com.dentalcare.controller;

import com.dentalcare.dto.response.AuditoriaResponse;
import com.dentalcare.service.IAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auditoria")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Auditor\u00eda", description = "Registro de auditor\u00eda del sistema (solo ADMIN)")
public class AuditoriaController {

    private final IAuditoriaService auditoriaService;

    public AuditoriaController(IAuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar auditor\u00eda", description = "Lista paginada del registro de auditor\u00eda")
    public ResponseEntity<Page<AuditoriaResponse>> listar(@PageableDefault(size = 20) Pageable pageable) {
        Page<AuditoriaResponse> auditoria = auditoriaService.listar(pageable);
        return ResponseEntity.ok(auditoria);
    }
}
