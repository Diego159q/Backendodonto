package com.dentalcare.service;

import com.dentalcare.dto.request.NotificacionRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.NotificacionResponse;

import java.util.List;

public interface INotificacionService {
    List<NotificacionResponse> listarPorUsuario(Long usuarioId);
    MensajeResponse marcarComoLeida(Long id);
    Long contarNoLeidas(Long usuarioId);
    MensajeResponse crear(NotificacionRequest request);
}
