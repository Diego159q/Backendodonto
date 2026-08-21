package com.dentalcare.service.impl;

import com.dentalcare.dto.request.HorarioAtencionRequest;
import com.dentalcare.dto.response.HorarioAtencionResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.HorarioAtencion;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.HorarioAtencionRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.service.IHorarioAtencionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HorarioAtencionServiceImpl implements IHorarioAtencionService {

    private final HorarioAtencionRepository horarioAtencionRepository;
    private final OdontologoRepository odontologoRepository;

    public HorarioAtencionServiceImpl(HorarioAtencionRepository horarioAtencionRepository, OdontologoRepository odontologoRepository) {
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioAtencionResponse> listarTodos() {
        return horarioAtencionRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioAtencionResponse> listarPorOdontologo(Long odontologoId) {
        return horarioAtencionRepository.findAll().stream()
                .filter(h -> h.getOdontologo().getId().equals(odontologoId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioAtencionResponse obtenerPorId(Long id) {
        return horarioAtencionRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Horario de atención no encontrado con id: " + id));
    }

    @Override
    public MensajeResponse crear(HorarioAtencionRequest request) {
        if (request.getHoraFin().isBefore(request.getHoraInicio()) || request.getHoraFin().equals(request.getHoraInicio())) {
            throw new BadRequestException("La hora de fin debe ser mayor a la hora de inicio");
        }

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + request.getOdontologoId()));

        validarSuperposicion(request, null);

        HorarioAtencion horario = new HorarioAtencion();
        horario.setOdontologo(odontologo);
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setActivo(request.getActivo());
        
        horarioAtencionRepository.save(horario);
        return MensajeResponse.builder().mensaje("Horario creado correctamente").success(true).timestamp(java.time.LocalDateTime.now()).build();
    }

    @Override
    public MensajeResponse actualizar(Long id, HorarioAtencionRequest request) {
        if (request.getHoraFin().isBefore(request.getHoraInicio()) || request.getHoraFin().equals(request.getHoraInicio())) {
            throw new BadRequestException("La hora de fin debe ser mayor a la hora de inicio");
        }

        HorarioAtencion horario = horarioAtencionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + id));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id: " + request.getOdontologoId()));

        validarSuperposicion(request, id);

        horario.setOdontologo(odontologo);
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setActivo(request.getActivo());

        horarioAtencionRepository.save(horario);
        return MensajeResponse.builder().mensaje("Horario actualizado correctamente").success(true).timestamp(java.time.LocalDateTime.now()).build();
    }

    @Override
    public MensajeResponse eliminar(Long id) {
        if (!horarioAtencionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horario no encontrado con id: " + id);
        }
        horarioAtencionRepository.deleteById(id);
        return MensajeResponse.builder().mensaje("Horario eliminado correctamente").success(true).timestamp(java.time.LocalDateTime.now()).build();
    }

    private void validarSuperposicion(HorarioAtencionRequest request, Long horarioIgnoradoId) {
        List<HorarioAtencion> horariosExistentes = horarioAtencionRepository.findByActivoTrueAndDiaSemana(request.getDiaSemana()).stream()
                .filter(h -> h.getOdontologo().getId().equals(request.getOdontologoId()))
                .filter(h -> horarioIgnoradoId == null || !h.getId().equals(horarioIgnoradoId))
                .collect(Collectors.toList());

        for (HorarioAtencion h : horariosExistentes) {
            if (request.getHoraInicio().isBefore(h.getHoraFin()) && request.getHoraFin().isAfter(h.getHoraInicio())) {
                throw new DuplicateResourceException("El bloque horario se superpone con un horario existente para este día y odontólogo.");
            }
        }
    }

    private HorarioAtencionResponse mapToResponse(HorarioAtencion horario) {
        return HorarioAtencionResponse.builder()
                .id(horario.getId())
                .odontologoId(horario.getOdontologo().getId())
                .odontologoNombre(horario.getOdontologo().getNombres() + " " + horario.getOdontologo().getApellidos())
                .diaSemana(horario.getDiaSemana())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                .activo(horario.getActivo())
                .build();
    }
}
