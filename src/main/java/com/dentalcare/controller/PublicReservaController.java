package com.dentalcare.controller;

import com.dentalcare.dto.request.PublicReservaRequest;
import com.dentalcare.dto.response.PublicDisponibilidadResponse;
import com.dentalcare.dto.response.PublicReservaResponse;
import com.dentalcare.dto.response.PublicServicioResponse;
import com.dentalcare.service.IPublicReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/public/reservas")
@Tag(name = "Reservas Públicas", description = "Endpoints seguros para la web pública")
public class PublicReservaController {

    private final IPublicReservaService publicReservaService;

    public PublicReservaController(IPublicReservaService publicReservaService) {
        this.publicReservaService = publicReservaService;
    }

    @GetMapping("/servicios")
    @Operation(summary = "Catálogo de servicios", description = "Obtiene los servicios públicos disponibles")
    public ResponseEntity<List<PublicServicioResponse>> listarServicios() {
        return ResponseEntity.ok(publicReservaService.listarServicios());
    }

    @GetMapping("/disponibilidad")
    @Operation(summary = "Consultar disponibilidad", description = "Obtiene los horarios libres sin revelar info interna")
    public ResponseEntity<PublicDisponibilidadResponse> obtenerDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long tratamientoId) {
        return ResponseEntity.ok(publicReservaService.obtenerDisponibilidad(fecha, tratamientoId));
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar cita", description = "Crea una cita como paciente no autenticado")
    public ResponseEntity<PublicReservaResponse> agendarCita(@Valid @RequestBody PublicReservaRequest request) {
        PublicReservaResponse response = publicReservaService.agendarCita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
