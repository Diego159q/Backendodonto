package com.dentalcare.service;

import com.dentalcare.dto.request.MedicamentoRequest;
import com.dentalcare.dto.response.MedicamentoResponse;
import com.dentalcare.dto.response.MensajeResponse;

import java.util.List;

public interface IMedicamentoService {
    List<MedicamentoResponse> listar();
    MensajeResponse crear(MedicamentoRequest request);
    MensajeResponse actualizar(Long id, MedicamentoRequest request);
}
