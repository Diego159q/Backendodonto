package com.dentalcare.controller;

import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.NotificacionResponse;
import com.dentalcare.service.INotificacionService;
import com.dentalcare.security.CustomUserDetailsService.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Notificaciones", description = "Gesti\u00f3n de notificaciones del sistema")
public class NotificacionController {

    private final INotificacionService notificacionService;

    public NotificacionController(INotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones", description = "Obtiene las notificaciones del usuario autenticado")
    public ResponseEntity<List<NotificacionResponse>> listar(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<NotificacionResponse> notificaciones = notificacionService.listarPorUsuario(userPrincipal.getUserId());
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/no-leidas")
    @Operation(summary = "Contar no le\u00eddas", description = "Obtiene la cantidad de notificaciones no le\u00eddas del usuario autenticado")
    public ResponseEntity<Long> contarNoLeidas(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long count = notificacionService.contarNoLeidas(userPrincipal.getUserId());
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{id}/leer")
    @Operation(summary = "Marcar como le\u00edda", description = "Marca una notificaci\u00f3n como le\u00edda")
    public ResponseEntity<MensajeResponse> marcarComoLeida(@PathVariable Long id) {
        MensajeResponse response = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(response);
    }
}
