package com.dentalcare.service;

import com.dentalcare.dto.request.BloqueoAgendaRequest;
import com.dentalcare.dto.response.BloqueoAgendaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import java.util.List;

public interface IBloqueoAgendaService {
    List<BloqueoAgendaResponse> listarTodos();
    BloqueoAgendaResponse obtenerPorId(Long id);
    MensajeResponse crear(BloqueoAgendaRequest request);
    MensajeResponse actualizar(Long id, BloqueoAgendaRequest request);
    MensajeResponse eliminar(Long id);
}
