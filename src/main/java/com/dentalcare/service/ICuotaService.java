package com.dentalcare.service;

import com.dentalcare.dto.request.CuotaRequest;
import com.dentalcare.dto.response.CuotaResponse;
import com.dentalcare.dto.response.MensajeResponse;

import java.util.List;

public interface ICuotaService {
    List<CuotaResponse> listarPorPlan(Long planTratamientoId);
    MensajeResponse crear(CuotaRequest request);
    MensajeResponse pagarCuota(Long cuotaId, Long pagoId);
    List<CuotaResponse> listarCuotasVencidas();
    MensajeResponse actualizarCuotasVencidas();
}
