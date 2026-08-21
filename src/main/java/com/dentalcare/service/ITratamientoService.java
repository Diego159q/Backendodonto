package com.dentalcare.service;

import com.dentalcare.dto.request.TratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.TratamientoResponse;

import java.util.List;

public interface ITratamientoService {
    List<TratamientoResponse> listar();
    TratamientoResponse obtenerPorId(Long id);
    MensajeResponse crear(TratamientoRequest request);
    MensajeResponse actualizar(Long id, TratamientoRequest request);
}
