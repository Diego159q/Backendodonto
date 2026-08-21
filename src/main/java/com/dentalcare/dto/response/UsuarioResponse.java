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
public class UsuarioResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String username;
    private String telefono;
    private String rol;
    private Long rolId;
    private Boolean activo;
    private Boolean bloqueado;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;
}
