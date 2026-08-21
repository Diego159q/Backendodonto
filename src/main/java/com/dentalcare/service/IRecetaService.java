package com.dentalcare.service;

import com.dentalcare.dto.request.RecetaRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.RecetaResponse;

import java.util.List;

public interface IRecetaService {
    List<RecetaResponse> listar(Long pacienteId);
    RecetaResponse obtenerPorId(Long id);
    MensajeResponse crear(RecetaRequest request);
    MensajeResponse aprobar(Long id);
    MensajeResponse anular(Long id);
}
