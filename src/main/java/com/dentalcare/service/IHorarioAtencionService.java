package com.dentalcare.service;

import com.dentalcare.dto.request.HorarioAtencionRequest;
import com.dentalcare.dto.response.HorarioAtencionResponse;
import com.dentalcare.dto.response.MensajeResponse;
import java.util.List;

public interface IHorarioAtencionService {
    List<HorarioAtencionResponse> listarTodos();
    List<HorarioAtencionResponse> listarPorOdontologo(Long odontologoId);
    HorarioAtencionResponse obtenerPorId(Long id);
    MensajeResponse crear(HorarioAtencionRequest request);
    MensajeResponse actualizar(Long id, HorarioAtencionRequest request);
    MensajeResponse eliminar(Long id);
}
