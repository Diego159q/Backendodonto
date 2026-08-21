package com.dentalcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OdontologoRequest {
    @NotNull
    private Long usuarioId;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String dni;

    private String telefono;

    @Email
    private String email;

    private String especialidad;

    @NotBlank
    private String numeroColegiatura;

    private String horarioAtencion;

    private String firmaUrl;
}
