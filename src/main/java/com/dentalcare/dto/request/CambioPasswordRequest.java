package com.dentalcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioPasswordRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String passwordActual;

    @NotBlank
    @Size(min = 6)
    private String passwordNueva;
}
