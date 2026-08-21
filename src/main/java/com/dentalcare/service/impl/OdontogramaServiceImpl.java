package com.dentalcare.service.impl;

import com.dentalcare.dto.request.OdontogramaDetalleRequest;
import com.dentalcare.dto.request.OdontogramaRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.OdontogramaResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.OdontogramaDetalleRepository;
import com.dentalcare.repository.OdontogramaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.service.IOdontogramaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OdontogramaServiceImpl implements IOdontogramaService {

    private final OdontogramaRepository odontogramaRepository;
    private final OdontogramaDetalleRepository odontogramaDetalleRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final MapperUtil mapperUtil;

    public OdontogramaServiceImpl(OdontogramaRepository odontogramaRepository,
                                  OdontogramaDetalleRepository odontogramaDetalleRepository,
                                  PacienteRepository pacienteRepository,
                                  OdontologoRepository odontologoRepository,
                                  MapperUtil mapperUtil) {
        this.odontogramaRepository = odontogramaRepository;
        this.odontogramaDetalleRepository = odontogramaDetalleRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public OdontogramaResponse obtenerPorId(Long id) {
        Odontograma odontograma = odontogramaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontograma", "id", id));
        return mapperUtil.toOdontogramaResponse(odontograma);
    }

    @Override
    @Transactional(readOnly = true)
    public OdontogramaResponse obtenerActualPorPaciente(Long pacienteId) {
        List<Odontograma> odontogramas = odontogramaRepository.findByPacienteId(pacienteId);
        return odontogramas.stream()
                .filter(o -> o.getActivo() != null && o.getActivo())
                .findFirst()
                .map(mapperUtil::toOdontogramaResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Odontograma", "pacienteId", pacienteId));
    }

    @Override
    public MensajeResponse crear(OdontogramaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        Odontograma odontograma = new Odontograma();
        odontograma.setPaciente(paciente);
        odontograma.setOdontologo(odontologo);
        odontograma.setFecha(LocalDate.now());
        odontograma.setTipoDenticion(request.getTipoDenticion());
        odontograma.setObservaciones(request.getObservaciones());
        odontograma.setEstado("ACTIVO");
        odontograma.setActivo(true);

        odontogramaRepository.save(odontograma);

        return MensajeResponse.builder()
                .mensaje("Odontograma creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse agregarDetalle(Long odontogramaId, OdontogramaDetalleRequest request) {
        Odontograma odontograma = odontogramaRepository.findById(odontogramaId)
                .orElseThrow(() -> new ResourceNotFoundException("Odontograma", "id", odontogramaId));

        OdontogramaDetalle detalle = new OdontogramaDetalle();
        detalle.setOdontograma(odontograma);
        detalle.setNumeroPieza(request.getNumeroPieza());
        detalle.setCaraDental(request.getCaraDental());
        detalle.setCondicion(CondicionDental.valueOf(request.getCondicion()));
        detalle.setDescripcion(request.getDescripcion());
        detalle.setColor(request.getColor());

        if (request.getTratamientoPendiente() != null) {
            detalle.setTratamientoPendiente("S\u00ed".equalsIgnoreCase(request.getTratamientoPendiente()) ||
                    "true".equalsIgnoreCase(request.getTratamientoPendiente()));
        }
        if (request.getTratamientoRealizado() != null) {
            detalle.setTratamientoRealizado("S\u00ed".equalsIgnoreCase(request.getTratamientoRealizado()) ||
                    "true".equalsIgnoreCase(request.getTratamientoRealizado()));
        }

        odontogramaDetalleRepository.save(detalle);

        return MensajeResponse.builder()
                .mensaje("Detalle agregado al odontograma exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizarDetalles(Long odontogramaId, List<OdontogramaDetalleRequest> detalles) {
        Odontograma odontograma = odontogramaRepository.findById(odontogramaId)
                .orElseThrow(() -> new ResourceNotFoundException("Odontograma", "id", odontogramaId));

        List<OdontogramaDetalle> existingDetalles = odontogramaDetalleRepository.findByOdontogramaId(odontogramaId);
        odontogramaDetalleRepository.deleteAll(existingDetalles);

        for (OdontogramaDetalleRequest req : detalles) {
            OdontogramaDetalle detalle = new OdontogramaDetalle();
            detalle.setOdontograma(odontograma);
            detalle.setNumeroPieza(req.getNumeroPieza());
            detalle.setCaraDental(req.getCaraDental());
            detalle.setCondicion(CondicionDental.valueOf(req.getCondicion()));
            detalle.setDescripcion(req.getDescripcion());
            detalle.setColor(req.getColor());
            if (req.getTratamientoPendiente() != null) {
                detalle.setTratamientoPendiente("S\u00ed".equalsIgnoreCase(req.getTratamientoPendiente()) ||
                        "true".equalsIgnoreCase(req.getTratamientoPendiente()));
            }
            if (req.getTratamientoRealizado() != null) {
                detalle.setTratamientoRealizado("S\u00ed".equalsIgnoreCase(req.getTratamientoRealizado()) ||
                        "true".equalsIgnoreCase(req.getTratamientoRealizado()));
            }
            odontogramaDetalleRepository.save(detalle);
        }

        return MensajeResponse.builder()
                .mensaje("Detalles del odontograma actualizados exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
