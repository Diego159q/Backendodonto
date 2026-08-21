package com.dentalcare.controller;

import com.dentalcare.dto.request.UsuarioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.UsuarioResponse;
import com.dentalcare.service.IUsuarioService;
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
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Usuarios", description = "Gesti\u00f3n de usuarios del sistema (solo ADMIN)")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista paginada de todos los usuarios")
    public ResponseEntity<Page<UsuarioResponse>> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<UsuarioResponse> usuarios = usuarioService.listar(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene los detalles de un usuario espec\u00edfico")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        MensajeResponse response = usuarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        MensajeResponse response = usuarioService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado", description = "Activa o desactiva un usuario")
    public ResponseEntity<MensajeResponse> cambiarEstado(@PathVariable Long id) {
        MensajeResponse response = usuarioService.cambiarEstado(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/rol/{rolId}")
    @Operation(summary = "Asignar rol", description = "Asigna un rol a un usuario")
    public ResponseEntity<MensajeResponse> asignarRol(@PathVariable Long id, @PathVariable Long rolId) {
        MensajeResponse response = usuarioService.asignarRol(id, rolId);
        return ResponseEntity.ok(response);
    }
}
