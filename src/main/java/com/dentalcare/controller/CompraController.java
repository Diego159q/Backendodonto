package com.dentalcare.controller;

import com.dentalcare.dto.request.CompraRequest;
import com.dentalcare.dto.response.CompraResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.service.ICompraService;
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
@RequestMapping("/compras")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Compras", description = "Gesti\u00f3n de compras a proveedores (solo ADMIN)")
public class CompraController {

    private final ICompraService compraService;

    public CompraController(ICompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    @Operation(summary = "Listar compras", description = "Obtiene todas las compras registradas")
    public ResponseEntity<List<CompraResponse>> listar() {
        List<CompraResponse> compras = compraService.listar();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de una compra")
    public ResponseEntity<CompraResponse> obtenerPorId(@PathVariable Long id) {
        CompraResponse compra = compraService.obtenerPorId(id);
        return ResponseEntity.ok(compra);
    }

    @PostMapping
    @Operation(summary = "Registrar compra", description = "Registra una nueva compra con sus detalles")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody CompraRequest request, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MensajeResponse response = compraService.crear(request, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
