package com.dentalcare.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraRequest {
    @NotNull
    private Long proveedorId;

    private LocalDate fecha;

    private String numeroDocumento;

    private String observaciones;

    private List<CompraDetalleRequest> detalles;
}
