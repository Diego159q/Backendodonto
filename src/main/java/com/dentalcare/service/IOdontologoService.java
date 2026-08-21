package com.dentalcare.service;

import com.dentalcare.dto.request.OdontologoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.OdontologoResponse;

import java.util.List;

public interface IOdontologoService {
    List<OdontologoResponse> listar();
    OdontologoResponse obtenerPorId(Long id);
    MensajeResponse crear(OdontologoRequest request);
    MensajeResponse actualizar(Long id, OdontologoRequest request);
    MensajeResponse cambiarEstado(Long id);
}
