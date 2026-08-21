package com.dentalcare.service;

import com.dentalcare.dto.request.CancelarCitaRequest;
import com.dentalcare.dto.request.CitaRequest;
import com.dentalcare.dto.request.ReprogramarCitaRequest;
import com.dentalcare.dto.response.CitaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ICitaService {
    Page<CitaResponse> listar(Long pacienteId, Long odontologoId, LocalDate fecha, String estado, Pageable pageable);
    CitaResponse obtenerPorId(Long id);
    MensajeResponse crear(CitaRequest request);
    MensajeResponse actualizar(Long id, CitaRequest request);
    MensajeResponse cancelar(Long id, CancelarCitaRequest request);
    MensajeResponse confirmar(Long id);
    MensajeResponse reprogramar(Long id, ReprogramarCitaRequest request);
    MensajeResponse marcarAsistio(Long id);
    MensajeResponse marcarNoAsistio(Long id);
    List<String> obtenerHorariosDisponibles(Long odontologoId, LocalDate fecha);
    List<CitaResponse> obtenerCitasDelDia();
    List<CitaResponse> obtenerProximasCitas();
}
