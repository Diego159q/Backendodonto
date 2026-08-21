package com.dentalcare.service;

import com.dentalcare.dto.request.HistoriaClinicaRequest;
import com.dentalcare.dto.response.HistoriaClinicaResponse;
import com.dentalcare.dto.response.MensajeResponse;

import java.util.List;

public interface IHistoriaClinicaService {
    List<HistoriaClinicaResponse> listarPorPaciente(Long pacienteId);
    HistoriaClinicaResponse obtenerPorId(Long id);
    MensajeResponse crear(HistoriaClinicaRequest request);
    MensajeResponse actualizar(Long id, HistoriaClinicaRequest request);
}
