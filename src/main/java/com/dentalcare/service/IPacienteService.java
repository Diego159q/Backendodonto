package com.dentalcare.service;

import com.dentalcare.dto.request.PacienteRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPacienteService {
    Page<PacienteResponse> listar(String search, Pageable pageable);
    PacienteResponse obtenerPorId(Long id);
    PacienteResponse obtenerPorDni(String dni);
    MensajeResponse crear(PacienteRequest request);
    MensajeResponse actualizar(Long id, PacienteRequest request);
    MensajeResponse eliminar(Long id);
    List<PacienteResponse> buscar(String termino);
}
