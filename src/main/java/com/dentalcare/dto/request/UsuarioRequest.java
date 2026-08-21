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
public class UsuarioRequest {
    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    private String password;

    private String telefono;

    @NotNull
    private Long rolId;

    private Boolean activo;
}
