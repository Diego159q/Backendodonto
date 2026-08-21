package com.dentalcare.service;

import com.dentalcare.dto.request.ConfiguracionRequest;
import com.dentalcare.dto.response.ConfiguracionResponse;
import com.dentalcare.dto.response.MensajeResponse;

public interface IConfiguracionService {
    ConfiguracionResponse obtenerConfiguracion();
    MensajeResponse actualizar(ConfiguracionRequest request);
}
