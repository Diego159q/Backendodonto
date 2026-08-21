package com.dentalcare.service;

import com.dentalcare.dto.request.OdontogramaDetalleRequest;
import com.dentalcare.dto.request.OdontogramaRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.OdontogramaResponse;

import java.util.List;

public interface IOdontogramaService {
    OdontogramaResponse obtenerPorId(Long id);
    OdontogramaResponse obtenerActualPorPaciente(Long pacienteId);
    MensajeResponse crear(OdontogramaRequest request);
    MensajeResponse agregarDetalle(Long odontogramaId, OdontogramaDetalleRequest request);
    MensajeResponse actualizarDetalles(Long odontogramaId, List<OdontogramaDetalleRequest> detalles);
}
