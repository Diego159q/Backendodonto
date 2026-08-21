package com.dentalcare.service;

import com.dentalcare.dto.request.PacienteTratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteTratamientoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPacienteTratamientoService {
    Page<PacienteTratamientoResponse> listar(Long pacienteId, String estado, Pageable pageable);
    PacienteTratamientoResponse obtenerPorId(Long id);
    MensajeResponse crear(PacienteTratamientoRequest request);
    MensajeResponse actualizar(Long id, PacienteTratamientoRequest request);
    MensajeResponse actualizarEstado(Long id, String estado);
    MensajeResponse registrarSesion(Long id);
}
