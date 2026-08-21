package com.dentalcare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    private Long rolId;
}
