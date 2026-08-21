package com.dentalcare.service.impl;

import com.dentalcare.dto.request.CancelarCitaRequest;
import com.dentalcare.dto.request.CitaRequest;
import com.dentalcare.dto.request.ReprogramarCitaRequest;
import com.dentalcare.dto.response.CitaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.Cita;
import com.dentalcare.entity.EstadoCita;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.entity.Paciente;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.CitaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.service.ICitaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CitaServiceImpl implements ICitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final MapperUtil mapperUtil;

    public CitaServiceImpl(CitaRepository citaRepository,
                           PacienteRepository pacienteRepository,
                           OdontologoRepository odontologoRepository,
                           MapperUtil mapperUtil) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CitaResponse> listar(Long pacienteId, Long odontologoId, LocalDate fecha,
                                     String estado, Pageable pageable) {
        List<Cita> citas;

        if (fecha != null) {
            citas = citaRepository.findByFechaBetween(fecha, fecha);
        } else if (pacienteId != null) {
            citas = citaRepository.findByPacienteId(pacienteId);
        } else if (odontologoId != null) {
            citas = citaRepository.findByOdontologoId(odontologoId);
        } else if (estado != null && !estado.isEmpty()) {
            citas = citaRepository.findByEstado(estado);
        } else {
            citas = citaRepository.findAll();
        }

        if (estado != null && !estado.isEmpty()) {
            citas = citas.stream()
                    .filter(c -> c.getEstado().name().equals(estado))
                    .collect(Collectors.toList());
        }

        citas.sort((a, b) -> b.getFechaCreacion().compareTo(a.getFechaCreacion()));

        List<CitaResponse> dtos = mapperUtil.toCitaResponseList(citas);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<CitaResponse> pageContent = start < dtos.size() ? dtos.subList(start, end) : new ArrayList<>();
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));
        return mapperUtil.toCitaResponse(cita);
    }

    @Override
    public MensajeResponse crear(CitaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("No se pueden agendar citas en fechas pasadas");
        }

        if (request.getHoraInicio().isBefore(LocalTime.of(7, 0)) ||
                request.getHoraFin().isAfter(LocalTime.of(20, 0))) {
            throw new BadRequestException("Las horas deben estar entre 07:00 y 20:00");
        }

        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new BadRequestException("La hora de inicio debe ser anterior a la hora de fin");
        }

        List<Cita> existingCitas = citaRepository.findByFechaAndOdontologoIdOrderByHoraInicio(
                request.getFecha(), request.getOdontologoId());
        for (Cita existing : existingCitas) {
            if (isOverlapping(existing.getHoraInicio(), existing.getHoraFin(),
                    request.getHoraInicio(), request.getHoraFin())) {
                throw new DuplicateResourceException("El horario seleccionado se superpone con otra cita existente");
            }
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setOdontologo(odontologo);
        cita.setFecha(request.getFecha());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(request.getHoraFin());
        cita.setMotivo(request.getMotivo());
        cita.setTipoAtencion(request.getTipoAtencion());
        cita.setConsultorio(request.getConsultorio());
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setObservaciones(request.getObservaciones());

        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Cita creada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, CitaRequest request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        cita.setPaciente(paciente);
        cita.setOdontologo(odontologo);
        cita.setFecha(request.getFecha());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(request.getHoraFin());
        cita.setMotivo(request.getMotivo());
        cita.setTipoAtencion(request.getTipoAtencion());
        cita.setConsultorio(request.getConsultorio());
        cita.setObservaciones(request.getObservaciones());

        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Cita actualizada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse cancelar(Long id, CancelarCitaRequest request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));

        cita.setEstado(EstadoCita.CANCELADA);
        cita.setMotivoCancelacion(request.getMotivoCancelacion());
        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Cita cancelada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse confirmar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));

        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new BadRequestException("Solo se pueden confirmar citas en estado PENDIENTE");
        }

        cita.setEstado(EstadoCita.CONFIRMADA);
        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Cita confirmada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse reprogramar(Long id, ReprogramarCitaRequest request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));

        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("No se pueden reprogramar citas a fechas pasadas");
        }

        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new BadRequestException("La hora de inicio debe ser anterior a la hora de fin");
        }

        cita.setFecha(request.getFecha());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(request.getHoraFin());
        cita.setEstado(EstadoCita.REPROGRAMADA);
        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Cita reprogramada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse marcarAsistio(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));

        cita.setEstado(EstadoCita.ATENDIDA);
        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Asistencia registrada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse marcarNoAsistio(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));

        cita.setEstado(EstadoCita.NO_ASISTIO);
        citaRepository.save(cita);

        return MensajeResponse.builder()
                .mensaje("Inasistencia registrada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> obtenerHorariosDisponibles(Long odontologoId, LocalDate fecha) {
        List<Cita> citas = citaRepository.findByFechaAndOdontologoIdOrderByHoraInicio(fecha, odontologoId);

        List<String> horariosDisponibles = new ArrayList<>();
        String[] horarios = {"07:00", "08:00", "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"};

        for (String hora : horarios) {
            LocalTime horaInicio = LocalTime.parse(hora + ":00");
            LocalTime horaFin = horaInicio.plusHours(1);
            boolean ocupado = false;
            for (Cita cita : citas) {
                if (isOverlapping(cita.getHoraInicio(), cita.getHoraFin(), horaInicio, horaFin)) {
                    ocupado = true;
                    break;
                }
            }
            if (!ocupado) {
                horariosDisponibles.add(hora);
            }
        }

        return horariosDisponibles;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasDelDia() {
        List<Cita> citas = citaRepository.findByFecha(LocalDate.now());
        citas.sort((a, b) -> a.getHoraInicio().compareTo(b.getHoraInicio()));
        return mapperUtil.toCitaResponseList(citas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerProximasCitas() {
        LocalDate today = LocalDate.now();
        List<Cita> citas = citaRepository.findByFechaBetween(today, today.plusDays(7));
        citas.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
        return citas.stream()
                .filter(c -> c.getEstado() != EstadoCita.CANCELADA)
                .limit(10)
                .map(mapperUtil::toCitaResponse)
                .collect(Collectors.toList());
    }

    private boolean isOverlapping(LocalTime existingStart, LocalTime existingEnd,
                                  LocalTime newStart, LocalTime newEnd) {
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }
}

