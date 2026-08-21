package com.dentalcare.dto.request;

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
public class RestablecerPasswordRequest {
    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6)
    private String password;
}
