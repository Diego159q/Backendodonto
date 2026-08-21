package com.dentalcare.service;

import com.dentalcare.dto.request.EvolucionClinicaRequest;
import com.dentalcare.dto.response.EvolucionClinicaResponse;
import com.dentalcare.dto.response.MensajeResponse;

import java.util.List;

public interface IEvolucionClinicaService {
    List<EvolucionClinicaResponse> listarPorHistoriaClinica(Long historiaClinicaId);
    MensajeResponse crear(EvolucionClinicaRequest request);
}
