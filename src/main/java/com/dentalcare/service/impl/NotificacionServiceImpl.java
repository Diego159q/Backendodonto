package com.dentalcare.service.impl;

import com.dentalcare.dto.request.NotificacionRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.NotificacionResponse;
import com.dentalcare.entity.Notificacion;
import com.dentalcare.entity.Usuario;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.NotificacionRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.INotificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificacionServiceImpl implements INotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository,
                                   UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponse> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId)
                .stream()
                .map(this::toNotificacionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion", "id", id));

        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        return MensajeResponse.builder()
                .mensaje("Notificaci\u00f3n marcada como le\u00edda")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarNoLeidas(Long usuarioId) {
        return notificacionRepository.countByLeidaFalseAndUsuarioId(usuarioId);
    }

    @Override
    public MensajeResponse crear(NotificacionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo(request.getTitulo());
        notificacion.setMensaje(request.getMensaje());
        notificacion.setLeida(false);

        notificacionRepository.save(notificacion);

        return MensajeResponse.builder()
                .mensaje("Notificaci\u00f3n creada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private NotificacionResponse toNotificacionResponse(Notificacion notificacion) {
        return NotificacionResponse.builder()
                .id(notificacion.getId())
                .usuarioId(notificacion.getUsuario() != null ? notificacion.getUsuario().getId() : null)
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .leida(notificacion.getLeida())
                .fechaLectura(notificacion.getFechaLectura())
                .fechaCreacion(notificacion.getFechaCreacion())
                .build();
    }
}

