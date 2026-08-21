package com.dentalcare.controller;

import com.dentalcare.dto.request.MovimientoInventarioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.MovimientoInventarioResponse;
import com.dentalcare.service.IMovimientoInventarioService;
import com.dentalcare.security.CustomUserDetailsService.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/movimientos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Movimientos de Inventario", description = "Gesti\u00f3n de movimientos de inventario (solo ADMIN)")
public class MovimientoInventarioController {

    private final IMovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioController(IMovimientoInventarioService movimientoInventarioService) {
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar por producto", description = "Obtiene los movimientos de inventario de un producto")
    public ResponseEntity<List<MovimientoInventarioResponse>> listarPorProducto(@PathVariable Long productoId) {
        List<MovimientoInventarioResponse> movimientos = movimientoInventarioService.listarPorProducto(productoId);
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping
    @Operation(summary = "Registrar movimiento", description = "Registra un movimiento de entrada o salida de inventario")
    public ResponseEntity<MensajeResponse> registrar(@Valid @RequestBody MovimientoInventarioRequest request,
                                                      Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MensajeResponse response = movimientoInventarioService.registrar(request, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
