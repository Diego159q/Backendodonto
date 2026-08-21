package com.dentalcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorRequest {
    @NotBlank
    private String razonSocial;

    @NotBlank
    private String ruc;

    private String contacto;

    private String telefono;

    @Email
    private String email;

    private String direccion;

    private Boolean activo;
}
