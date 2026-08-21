package com.dentalcare.controller;

import com.dentalcare.dto.request.*;
import com.dentalcare.dto.response.*;
import com.dentalcare.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticaci\u00f3n", description = "Endpoints p\u00fablicos de autenticaci\u00f3n")
public class AuthController {

    private final IAuthService authService;
    private final com.dentalcare.security.CookieUtil cookieUtil;

    public AuthController(IAuthService authService, com.dentalcare.security.CookieUtil cookieUtil) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesi\u00f3n", description = "Autentica un usuario y devuelve sus datos, seteando el token en cookie HttpOnly")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, jakarta.servlet.http.HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);
        cookieUtil.createCookie(response, loginResponse.getToken());
        loginResponse.setToken(null); // Ocultar el token del body
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesi\u00f3n", description = "Limpia la cookie HttpOnly")
    public ResponseEntity<MensajeResponse> logout(jakarta.servlet.http.HttpServletResponse response) {
        cookieUtil.clearCookie(response);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Sesi\u00f3n cerrada exitosamente")
                .success(true)
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener usuario actual", description = "Devuelve los datos del usuario logueado en la sesión actual")
    public ResponseEntity<LoginResponse> me(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        
        LoginResponse response = authService.obtenerUsuarioActual(userDetails.getUsername());
                
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema (solo ADMIN)")
    public ResponseEntity<MensajeResponse> register(@Valid @RequestBody RegisterRequest request) {
        MensajeResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cambiar-password")
    @Operation(summary = "Cambiar contrase\u00f1a", description = "Cambia la contrase\u00f1a de un usuario autenticado")
    public ResponseEntity<MensajeResponse> cambiarPassword(@Valid @RequestBody CambioPasswordRequest request) {
        MensajeResponse response = authService.cambiarPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-password")
    @Operation(summary = "Recuperar contrase\u00f1a", description = "Env\u00eda un enlace para restablecer la contrase\u00f1a al correo del usuario")
    public ResponseEntity<MensajeResponse> recuperarPassword(@Valid @RequestBody RecuperarPasswordRequest request) {
        MensajeResponse response = authService.recuperarPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/restablecer-password")
    @Operation(summary = "Restablecer contrase\u00f1a", description = "Restablece la contrase\u00f1a usando un token v\u00e1lido")
    public ResponseEntity<MensajeResponse> restablecerPassword(@Valid @RequestBody RestablecerPasswordRequest request) {
        MensajeResponse response = authService.restablecerPassword(request);
        return ResponseEntity.ok(response);
    }
}
