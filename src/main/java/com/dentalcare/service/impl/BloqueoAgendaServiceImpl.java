package com.dentalcare.service.impl;

import com.dentalcare.dto.request.BloqueoAgendaRequest;
import com.dentalcare.dto.response.BloqueoAgendaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.BloqueoAgenda;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.BloqueoAgendaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.service.IBloqueoAgendaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BloqueoAgendaServiceImpl implements IBloqueoAgendaService {

    private final BloqueoAgendaRepository bloqueoAgendaRepository;
    private final OdontologoRepository odontologoRepository;

    public BloqueoAgendaServiceImpl(BloqueoAgendaRepository bloqueoAgendaRepository, OdontologoRepository odontologoRepository) {
        this.bloqueoAgendaRepository = bloqueoAgendaRepository;
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloqueoAgendaResponse> listarTodos() {
        return bloqueoAgendaRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BloqueoAgendaResponse obtenerPorId(Long id) {
        return bloqueoAgendaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado con id: " + id));
    }

    @Override
    public MensajeResponse crear(BloqueoAgendaRequest request) {
        if (request.getHoraInicio() != null && request.getHoraFin() != null) {
            if (request.getHoraFin().isBefore(request.getHoraInicio()) || request.getHoraFin().equals(request.getHoraInicio())) {
                throw new BadRequestException("La hora de fin debe ser mayor a la hora de inicio");
            }
        } else if (request.getHoraInicio() != null || request.getHoraFin() != null) {
            throw new BadRequestException("Debe especificar tanto hora de inicio como fin para bloquear un rango, o dejar ambos en null para bloquear todo el día");
        }

        BloqueoAgenda bloqueo = new BloqueoAgenda();
        if (request.getOdontologoId() != null) {
            Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + request.getOdontologoId()));
            bloqueo.setOdontologo(odontologo);
        }
        
        bloqueo.setFecha(request.getFecha());
        bloqueo.setHoraInicio(request.getHoraInicio());
        bloqueo.setHoraFin(request.getHoraFin());
        bloqueo.setMotivo(request.getMotivo());
        
        bloqueoAgendaRepository.save(bloqueo);
        return MensajeResponse.builder().mensaje("Bloqueo de agenda creado correctamente").success(true).timestamp(java.time.LocalDateTime.now()).build();
    }

    @Override
    public MensajeResponse actualizar(Long id, BloqueoAgendaRequest request) {
        BloqueoAgenda bloqueo = bloqueoAgendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado con id: " + id));

        if (request.getHoraInicio() != null && request.getHoraFin() != null) {
            if (request.getHoraFin().isBefore(request.getHoraInicio()) || request.getHoraFin().equals(request.getHoraInicio())) {
                throw new BadRequestException("La hora de fin debe ser mayor a la hora de inicio");
            }
        } else if (request.getHoraInicio() != null || request.getHoraFin() != null) {
            throw new BadRequestException("Debe especificar tanto hora de inicio como fin para bloquear un rango, o dejar ambos en null para bloquear todo el día");
        }

        if (request.getOdontologoId() != null) {
            Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + request.getOdontologoId()));
            bloqueo.setOdontologo(odontologo);
        } else {
            bloqueo.setOdontologo(null);
        }

        bloqueo.setFecha(request.getFecha());
        bloqueo.setHoraInicio(request.getHoraInicio());
        bloqueo.setHoraFin(request.getHoraFin());
        bloqueo.setMotivo(request.getMotivo());
        
        bloqueoAgendaRepository.save(bloqueo);
        return MensajeResponse.builder().mensaje("Bloqueo de agenda actualizado correctamente").success(true).timestamp(java.time.LocalDateTime.now()).build();
    }

    @Override
    public MensajeResponse eliminar(Long id) {
        if (!bloqueoAgendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bloqueo no encontrado con id: " + id);
        }
        bloqueoAgendaRepository.deleteById(id);
        return MensajeResponse.builder().mensaje("Bloqueo eliminado correctamente").success(true).timestamp(java.time.LocalDateTime.now()).build();
    }

    private BloqueoAgendaResponse mapToResponse(BloqueoAgenda bloqueo) {
        String ambito = (bloqueo.getOdontologo() == null) ? "Global" : "Específico";
        String odontologoNombre = (bloqueo.getOdontologo() != null) ? 
                bloqueo.getOdontologo().getNombres() + " " + bloqueo.getOdontologo().getApellidos() : "Todos";
        Long odontologoId = (bloqueo.getOdontologo() != null) ? bloqueo.getOdontologo().getId() : null;

        return BloqueoAgendaResponse.builder()
                .id(bloqueo.getId())
                .odontologoId(odontologoId)
                .odontologoNombre(odontologoNombre)
                .fecha(bloqueo.getFecha())
                .horaInicio(bloqueo.getHoraInicio())
                .horaFin(bloqueo.getHoraFin())
                .motivo(bloqueo.getMotivo())
                .ambito(ambito)
                .build();
    }
}
