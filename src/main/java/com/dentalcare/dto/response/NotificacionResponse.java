package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponse {
    private Long id;
    private Long usuarioId;
    private String titulo;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fechaLectura;
    private LocalDateTime fechaCreacion;
}
