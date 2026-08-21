package com.dentalcare.controller;

import com.dentalcare.dto.request.PagoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PagoResponse;
import com.dentalcare.service.IPagoService;
import com.dentalcare.security.CustomUserDetailsService.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pagos")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
@Tag(name = "Pagos", description = "Gesti\u00f3n de pagos y caja")
public class PagoController {

    private final IPagoService pagoService;

    public PagoController(IPagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    @Operation(summary = "Listar pagos", description = "Lista paginada de pagos con filtros opcionales")
    public ResponseEntity<Page<PagoResponse>> listar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<PagoResponse> pagos = pagoService.listar(pacienteId, fechaInicio, fechaFin, pageable);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de un pago")
    public ResponseEntity<PagoResponse> obtenerPorId(@PathVariable Long id) {
        PagoResponse pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    @Operation(summary = "Crear pago", description = "Registra un nuevo pago")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody PagoRequest request, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MensajeResponse response = pagoService.crear(request, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/deudas")
    @Operation(summary = "Deudas pendientes", description = "Obtiene la lista de pagos pendientes")
    public ResponseEntity<List<PagoResponse>> listarDeudas() {
        List<PagoResponse> deudas = pagoService.listarDeudasPendientes();
        return ResponseEntity.ok(deudas);
    }

    @GetMapping("/ingresos/dia")
    @Operation(summary = "Ingresos del d\u00eda", description = "Obtiene el total de ingresos del d\u00eda actual")
    public ResponseEntity<BigDecimal> ingresosDelDia() {
        BigDecimal ingresos = pagoService.obtenerIngresosDelDia();
        return ResponseEntity.ok(ingresos);
    }

    @GetMapping("/ingresos/mes")
    @Operation(summary = "Ingresos del mes", description = "Obtiene el total de ingresos del mes actual")
    public ResponseEntity<BigDecimal> ingresosDelMes() {
        BigDecimal ingresos = pagoService.obtenerIngresosDelMes();
        return ResponseEntity.ok(ingresos);
    }

    @GetMapping("/caja/{fecha}")
    @Operation(summary = "Caja por fecha", description = "Obtiene el detalle de caja de una fecha espec\u00edfica")
    public ResponseEntity<List<PagoResponse>> cajaPorFecha(@PathVariable LocalDate fecha) {
        List<PagoResponse> pagos = pagoService.obtenerPagosPorFecha(fecha);
        return ResponseEntity.ok(pagos);
    }
}
