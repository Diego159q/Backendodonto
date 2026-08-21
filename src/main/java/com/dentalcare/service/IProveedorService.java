package com.dentalcare.service;

import com.dentalcare.dto.request.ProveedorRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.ProveedorResponse;

import java.util.List;

public interface IProveedorService {
    List<ProveedorResponse> listar();
    ProveedorResponse obtenerPorId(Long id);
    MensajeResponse crear(ProveedorRequest request);
    MensajeResponse actualizar(Long id, ProveedorRequest request);
}
