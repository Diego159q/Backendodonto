package com.dentalcare.controller;

import com.dentalcare.dto.request.ArchivoClinicoRequest;
import com.dentalcare.dto.response.ArchivoClinicoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.IArchivoClinicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/archivos")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ODONTOLOGA')")
@Tag(name = "Archivos Cl\u00ednicos", description = "Gesti\u00f3n de archivos cl\u00ednicos de pacientes")
public class ArchivoClinicoController {

    private final IArchivoClinicoService archivoClinicoService;

    public ArchivoClinicoController(IArchivoClinicoService archivoClinicoService) {
        this.archivoClinicoService = archivoClinicoService;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar por paciente", description = "Obtiene todos los archivos cl\u00ednicos de un paciente")
    public ResponseEntity<List<ArchivoClinicoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<ArchivoClinicoResponse> archivos = archivoClinicoService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(archivos);
    }

    @PostMapping("/upload")
    @Operation(summary = "Subir archivo", description = "Sube un archivo cl\u00ednico para un paciente")
    public ResponseEntity<MensajeResponse> upload(
            @Valid @RequestPart("metadata") ArchivoClinicoRequest request,
            @RequestPart("file") MultipartFile file) {
        MensajeResponse response = archivoClinicoService.subirArchivo(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
