package com.dentalcare.service.impl;

import com.dentalcare.dto.request.PacienteTratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteTratamientoResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.*;
import com.dentalcare.service.IPacienteTratamientoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PacienteTratamientoServiceImpl implements IPacienteTratamientoService {

    private final PacienteTratamientoRepository pacienteTratamientoRepository;
    private final PacienteRepository pacienteRepository;
    private final TratamientoRepository tratamientoRepository;
    private final OdontologoRepository odontologoRepository;
    private final DiagnosticoRepository diagnosticoRepository;

    public PacienteTratamientoServiceImpl(PacienteTratamientoRepository pacienteTratamientoRepository,
                                          PacienteRepository pacienteRepository,
                                          TratamientoRepository tratamientoRepository,
                                          OdontologoRepository odontologoRepository,
                                          DiagnosticoRepository diagnosticoRepository) {
        this.pacienteTratamientoRepository = pacienteTratamientoRepository;
        this.pacienteRepository = pacienteRepository;
        this.tratamientoRepository = tratamientoRepository;
        this.odontologoRepository = odontologoRepository;
        this.diagnosticoRepository = diagnosticoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteTratamientoResponse> listar(Long pacienteId, String estado, Pageable pageable) {
        List<PacienteTratamiento> lista;
        if (pacienteId != null && estado != null && !estado.isEmpty()) {
            lista = pacienteTratamientoRepository.findByPacienteIdAndEstado(pacienteId, EstadoTratamiento.valueOf(estado));
        } else if (pacienteId != null) {
            lista = pacienteTratamientoRepository.findByPacienteId(pacienteId);
        } else if (estado != null && !estado.isEmpty()) {
            lista = pacienteTratamientoRepository.findByEstado(EstadoTratamiento.valueOf(estado));
        } else {
            lista = pacienteTratamientoRepository.findAll();
        }

        List<PacienteTratamientoResponse> dtos = lista.stream()
                .map(this::toPacienteTratamientoResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<PacienteTratamientoResponse> pageContent = start < dtos.size() ?
                dtos.subList(start, end) : List.of();
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteTratamientoResponse obtenerPorId(Long id) {
        PacienteTratamiento pt = pacienteTratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PacienteTratamiento", "id", id));
        return toPacienteTratamientoResponse(pt);
    }

    @Override
    public MensajeResponse crear(PacienteTratamientoRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        Tratamiento tratamiento = tratamientoRepository.findById(request.getTratamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento", "id", request.getTratamientoId()));

        BigDecimal precio = request.getPrecio() != null ? request.getPrecio() : tratamiento.getPrecioBase();
        BigDecimal descuento = request.getDescuento() != null ? request.getDescuento() : BigDecimal.ZERO;
        BigDecimal precioFinal = precio.subtract(descuento);

        PacienteTratamiento pt = new PacienteTratamiento();
        pt.setPaciente(paciente);
        pt.setOdontologo(odontologo);
        pt.setTratamiento(tratamiento);
        pt.setPiezaDental(request.getPiezaDental() != null ? Integer.parseInt(request.getPiezaDental()) : null);
        pt.setFechaInicio(request.getFechaInicio());
        pt.setFechaFinEstimada(request.getFechaFinEstimada());
        pt.setPrecio(precio);
        pt.setDescuento(descuento);
        pt.setPrecioFinal(precioFinal);
        pt.setNumeroSesiones(request.getNumeroSesiones() != null ? request.getNumeroSesiones() : tratamiento.getNumeroSesiones());
        pt.setSesionesRealizadas(0);
        pt.setPorcentajeAvance(0);
        pt.setEstado(EstadoTratamiento.PENDIENTE);
        pt.setObservaciones(request.getObservaciones());
        pt.setActivo(true);

        if (request.getDiagnosticoId() != null) {
            Diagnostico diagnostico = diagnosticoRepository.findById(request.getDiagnosticoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Diagnostico", "id", request.getDiagnosticoId()));
            pt.setDiagnostico(diagnostico);
        }

        pacienteTratamientoRepository.save(pt);

        return MensajeResponse.builder()
                .mensaje("Tratamiento asignado al paciente exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, PacienteTratamientoRequest request) {
        PacienteTratamiento pt = pacienteTratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PacienteTratamiento", "id", id));

        if (request.getOdontologoId() != null) {
            Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));
            pt.setOdontologo(odontologo);
        }
        if (request.getTratamientoId() != null) {
            Tratamiento tratamiento = tratamientoRepository.findById(request.getTratamientoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tratamiento", "id", request.getTratamientoId()));
            pt.setTratamiento(tratamiento);
        }

        pt.setPiezaDental(request.getPiezaDental() != null ? Integer.parseInt(request.getPiezaDental()) : null);
        if (request.getFechaInicio() != null) pt.setFechaInicio(request.getFechaInicio());
        if (request.getFechaFinEstimada() != null) pt.setFechaFinEstimada(request.getFechaFinEstimada());
        if (request.getPrecio() != null) pt.setPrecio(request.getPrecio());
        if (request.getDescuento() != null) pt.setDescuento(request.getDescuento());
        BigDecimal precioFinal = (request.getPrecio() != null ? request.getPrecio() : pt.getPrecio())
                .subtract(request.getDescuento() != null ? request.getDescuento() : pt.getDescuento());
        pt.setPrecioFinal(precioFinal);
        if (request.getNumeroSesiones() != null) pt.setNumeroSesiones(request.getNumeroSesiones());
        if (request.getObservaciones() != null) pt.setObservaciones(request.getObservaciones());

        pacienteTratamientoRepository.save(pt);

        return MensajeResponse.builder()
                .mensaje("Tratamiento actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizarEstado(Long id, String estado) {
        PacienteTratamiento pt = pacienteTratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PacienteTratamiento", "id", id));

        pt.setEstado(EstadoTratamiento.valueOf(estado));
        if (estado.equals("TERMINADO")) {
            pt.setPorcentajeAvance(100);
        }
        pacienteTratamientoRepository.save(pt);

        return MensajeResponse.builder()
                .mensaje("Estado del tratamiento actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse registrarSesion(Long id) {
        PacienteTratamiento pt = pacienteTratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PacienteTratamiento", "id", id));

        if (pt.getSesionesRealizadas() >= pt.getNumeroSesiones()) {
            throw new BadRequestException("Ya se realizaron todas las sesiones del tratamiento");
        }

        pt.setSesionesRealizadas(pt.getSesionesRealizadas() + 1);
        int avance = (pt.getSesionesRealizadas() * 100) / pt.getNumeroSesiones();
        pt.setPorcentajeAvance(Math.min(avance, 100));

        if (pt.getSesionesRealizadas() >= pt.getNumeroSesiones()) {
            pt.setEstado(EstadoTratamiento.TERMINADO);
        } else {
            pt.setEstado(EstadoTratamiento.EN_PROCESO);
        }

        pacienteTratamientoRepository.save(pt);

        return MensajeResponse.builder()
                .mensaje("Sesi\u00f3n registrada exitosamente. Avance: " + pt.getPorcentajeAvance() + "%")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private PacienteTratamientoResponse toPacienteTratamientoResponse(PacienteTratamiento pt) {
        return PacienteTratamientoResponse.builder()
                .id(pt.getId())
                .pacienteId(pt.getPaciente() != null ? pt.getPaciente().getId() : null)
                .pacienteNombre(pt.getPaciente() != null ?
                        pt.getPaciente().getNombres() + " " + pt.getPaciente().getApellidos() : null)
                .diagnosticoId(pt.getDiagnostico() != null ? pt.getDiagnostico().getId() : null)
                .tratamientoId(pt.getTratamiento() != null ? pt.getTratamiento().getId() : null)
                .tratamientoNombre(pt.getTratamiento() != null ? pt.getTratamiento().getNombre() : null)
                .odontologoId(pt.getOdontologo() != null ? pt.getOdontologo().getId() : null)
                .odontologoNombre(pt.getOdontologo() != null ?
                        pt.getOdontologo().getNombres() + " " + pt.getOdontologo().getApellidos() : null)
                .piezaDental(pt.getPiezaDental() != null ? String.valueOf(pt.getPiezaDental()) : null)
                .fechaInicio(pt.getFechaInicio())
                .fechaFinEstimada(pt.getFechaFinEstimada())
                .fechaFinReal(pt.getFechaFinReal())
                .precio(pt.getPrecio())
                .descuento(pt.getDescuento())
                .precioFinal(pt.getPrecioFinal())
                .numeroSesiones(pt.getNumeroSesiones())
                .sesionesRealizadas(pt.getSesionesRealizadas())
                .porcentajeAvance(pt.getPorcentajeAvance())
                .estado(pt.getEstado() != null ? pt.getEstado().name() : null)
                .observaciones(pt.getObservaciones())
                .build();
    }
}
