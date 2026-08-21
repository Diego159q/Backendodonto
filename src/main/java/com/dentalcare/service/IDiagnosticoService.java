package com.dentalcare.service;

import com.dentalcare.dto.request.DiagnosticoRequest;
import com.dentalcare.dto.response.DiagnosticoResponse;
import com.dentalcare.dto.response.MensajeResponse;

import java.util.List;

public interface IDiagnosticoService {
    List<DiagnosticoResponse> listar();
    DiagnosticoResponse obtenerPorId(Long id);
    MensajeResponse crear(DiagnosticoRequest request);
    MensajeResponse actualizar(Long id, DiagnosticoRequest request);
}
