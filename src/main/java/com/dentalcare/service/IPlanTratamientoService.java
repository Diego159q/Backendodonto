package com.dentalcare.service;

import com.dentalcare.dto.request.PlanTratamientoDetalleRequest;
import com.dentalcare.dto.request.PlanTratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PlanTratamientoResponse;

import java.util.List;

public interface IPlanTratamientoService {
    List<PlanTratamientoResponse> listarTodos();
    List<PlanTratamientoResponse> listarPorPaciente(Long pacienteId);
    PlanTratamientoResponse obtenerPorId(Long id);
    MensajeResponse crear(PlanTratamientoRequest request, List<PlanTratamientoDetalleRequest> detalles);
    MensajeResponse aceptarPlan(Long id);
    MensajeResponse agregarDetalle(Long planId, PlanTratamientoDetalleRequest request);
    MensajeResponse eliminarDetalle(Long detalleId);
}
