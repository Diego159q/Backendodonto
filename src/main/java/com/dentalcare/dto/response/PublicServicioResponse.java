package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicServicioResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    // Omitimos precioBase y sesiones para no revelar datos internos si no es deseado,
    // o se pueden incluir si la clinica es transparente. Por ahora lo mínimo.
}
