package com.dentalcare.service.impl;

import com.dentalcare.dto.request.EvolucionClinicaRequest;
import com.dentalcare.dto.response.EvolucionClinicaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.Cita;
import com.dentalcare.entity.EvolucionClinica;
import com.dentalcare.entity.HistoriaClinica;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.CitaRepository;
import com.dentalcare.repository.EvolucionClinicaRepository;
import com.dentalcare.repository.HistoriaClinicaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.service.IEvolucionClinicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvolucionClinicaServiceImpl implements IEvolucionClinicaService {

    private final EvolucionClinicaRepository evolucionClinicaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final OdontologoRepository odontologoRepository;
    private final CitaRepository citaRepository;

    public EvolucionClinicaServiceImpl(EvolucionClinicaRepository evolucionClinicaRepository,
                                       HistoriaClinicaRepository historiaClinicaRepository,
                                       OdontologoRepository odontologoRepository,
                                       CitaRepository citaRepository) {
        this.evolucionClinicaRepository = evolucionClinicaRepository;
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.odontologoRepository = odontologoRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucionClinicaResponse> listarPorHistoriaClinica(Long historiaClinicaId) {
        return evolucionClinicaRepository.findByHistoriaClinicaId(historiaClinicaId)
                .stream()
                .map(this::toEvolucionClinicaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse crear(EvolucionClinicaRequest request) {
        HistoriaClinica hc = historiaClinicaRepository.findById(request.getHistoriaClinicaId())
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", "id", request.getHistoriaClinicaId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        EvolucionClinica ec = new EvolucionClinica();
        ec.setHistoriaClinica(hc);
        ec.setOdontologo(odontologo);
        ec.setFecha(request.getFecha() != null ? request.getFecha() : LocalDate.now());
        ec.setDescripcion(request.getDescripcion());
        ec.setProcedimientoRealizado(request.getProcedimientoRealizado());
        ec.setObservaciones(request.getObservaciones());
        ec.setRecomendaciones(request.getRecomendaciones());

        if (request.getCitaId() != null) {
            Cita cita = citaRepository.findById(request.getCitaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", request.getCitaId()));
            ec.setCita(cita);
        }

        evolucionClinicaRepository.save(ec);

        return MensajeResponse.builder()
                .mensaje("Evoluci\u00f3n cl\u00ednica registrada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private EvolucionClinicaResponse toEvolucionClinicaResponse(EvolucionClinica ec) {
        return EvolucionClinicaResponse.builder()
                .id(ec.getId())
                .historiaClinicaId(ec.getHistoriaClinica() != null ? ec.getHistoriaClinica().getId() : null)
                .citaId(ec.getCita() != null ? ec.getCita().getId() : null)
                .odontologoId(ec.getOdontologo() != null ? ec.getOdontologo().getId() : null)
                .odontologoNombre(ec.getOdontologo() != null ?
                        ec.getOdontologo().getNombres() + " " + ec.getOdontologo().getApellidos() : null)
                .fecha(ec.getFecha())
                .descripcion(ec.getDescripcion())
                .procedimientoRealizado(ec.getProcedimientoRealizado())
                .observaciones(ec.getObservaciones())
                .recomendaciones(ec.getRecomendaciones())
                .fechaCreacion(ec.getFechaCreacion())
                .build();
    }
}

