package com.dentalcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionRequest {
    private String nombreCentro;

    private String ruc;

    private String direccion;

    private String telefono;

    private String email;

    private String logoUrl;

    private String horarioAtencion;

    private Integer duracionCitaPredeterminada;

    private String moneda;

    private String mensajeRecordatorio;

    private String nombreOdontologa;

    private String colegiatura;

    private String firmaUrl;
}
