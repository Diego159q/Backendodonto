package com.dentalcare.service.impl;

import com.dentalcare.dto.request.HistoriaClinicaRequest;
import com.dentalcare.dto.response.HistoriaClinicaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.HistoriaClinica;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.entity.Paciente;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.HistoriaClinicaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.service.IHistoriaClinicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoriaClinicaServiceImpl implements IHistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final MapperUtil mapperUtil;

    public HistoriaClinicaServiceImpl(HistoriaClinicaRepository historiaClinicaRepository,
                                      PacienteRepository pacienteRepository,
                                      OdontologoRepository odontologoRepository,
                                      MapperUtil mapperUtil) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriaClinicaResponse> listarPorPaciente(Long pacienteId) {
        return historiaClinicaRepository.findByPacienteIdOrderByFechaCreacionDesc(pacienteId)
                .stream()
                .map(this::toHistoriaClinicaResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriaClinicaResponse obtenerPorId(Long id) {
        HistoriaClinica hc = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", "id", id));
        return toHistoriaClinicaResponse(hc);
    }

    @Override
    public MensajeResponse crear(HistoriaClinicaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoResponsableId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoResponsableId()));

        HistoriaClinica hc = new HistoriaClinica();
        hc.setPaciente(paciente);
        hc.setOdontologoResponsable(odontologo);
        hc.setFechaApertura(request.getFechaApertura() != null ? request.getFechaApertura() : LocalDate.now());
        hc.setMotivoConsulta(request.getMotivoConsulta());
        hc.setEnfermedadActual(request.getEnfermedadActual());
        hc.setAntecedentesPersonales(request.getAntecedentesPersonales());
        hc.setAntecedentesFamiliares(request.getAntecedentesFamiliares());
        hc.setAlergias(request.getAlergias());
        hc.setEnfermedadesSistemicas(request.getEnfermedadesSistemicas());
        hc.setPresionArterial(request.getPresionArterial());
        hc.setPeso(BigDecimal.valueOf(request.getPeso()));
        hc.setTalla(BigDecimal.valueOf(request.getTalla()));
        hc.setTemperatura(BigDecimal.valueOf(request.getTemperatura()));
        hc.setDiagnosticoGeneral(request.getDiagnosticoGeneral());
        hc.setObservaciones(request.getObservaciones());
        hc.setRecomendaciones(request.getRecomendaciones());

        historiaClinicaRepository.save(hc);

        return MensajeResponse.builder()
                .mensaje("Historia cl\u00ednica creada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, HistoriaClinicaRequest request) {
        HistoriaClinica hc = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", "id", id));

        if (request.getOdontologoResponsableId() != null) {
            Odontologo odontologo = odontologoRepository.findById(request.getOdontologoResponsableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoResponsableId()));
            hc.setOdontologoResponsable(odontologo);
        }

        if (request.getFechaApertura() != null) hc.setFechaApertura(request.getFechaApertura());
        if (request.getMotivoConsulta() != null) hc.setMotivoConsulta(request.getMotivoConsulta());
        if (request.getEnfermedadActual() != null) hc.setEnfermedadActual(request.getEnfermedadActual());
        if (request.getAntecedentesPersonales() != null) hc.setAntecedentesPersonales(request.getAntecedentesPersonales());
        if (request.getAntecedentesFamiliares() != null) hc.setAntecedentesFamiliares(request.getAntecedentesFamiliares());
        if (request.getAlergias() != null) hc.setAlergias(request.getAlergias());
        if (request.getEnfermedadesSistemicas() != null) hc.setEnfermedadesSistemicas(request.getEnfermedadesSistemicas());
        if (request.getPresionArterial() != null) hc.setPresionArterial(request.getPresionArterial());
        if (request.getPeso() != null) hc.setPeso(BigDecimal.valueOf(request.getPeso()));
        if (request.getTalla() != null) hc.setTalla(BigDecimal.valueOf(request.getTalla()));
        if (request.getTemperatura() != null) hc.setTemperatura(BigDecimal.valueOf(request.getTemperatura()));
        if (request.getDiagnosticoGeneral() != null) hc.setDiagnosticoGeneral(request.getDiagnosticoGeneral());
        if (request.getObservaciones() != null) hc.setObservaciones(request.getObservaciones());
        if (request.getRecomendaciones() != null) hc.setRecomendaciones(request.getRecomendaciones());

        historiaClinicaRepository.save(hc);

        return MensajeResponse.builder()
                .mensaje("Historia cl\u00ednica actualizada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private HistoriaClinicaResponse toHistoriaClinicaResponse(HistoriaClinica hc) {
        return HistoriaClinicaResponse.builder()
                .id(hc.getId())
                .pacienteId(hc.getPaciente() != null ? hc.getPaciente().getId() : null)
                .pacienteNombre(hc.getPaciente() != null ?
                        hc.getPaciente().getNombres() + " " + hc.getPaciente().getApellidos() : null)
                .odontologoId(hc.getOdontologoResponsable() != null ? hc.getOdontologoResponsable().getId() : null)
                .odontologoNombre(hc.getOdontologoResponsable() != null ?
                        hc.getOdontologoResponsable().getNombres() + " " + hc.getOdontologoResponsable().getApellidos() : null)
                .fechaApertura(hc.getFechaApertura())
                .motivoConsulta(hc.getMotivoConsulta())
                .enfermedadActual(hc.getEnfermedadActual())
                .antecedentesPersonales(hc.getAntecedentesPersonales())
                .antecedentesFamiliares(hc.getAntecedentesFamiliares())
                .alergias(hc.getAlergias())
                .enfermedadesSistemicas(hc.getEnfermedadesSistemicas())
                .presionArterial(hc.getPresionArterial())
                .peso(hc.getPeso() != null ? hc.getPeso().doubleValue() : null)
                .talla(hc.getTalla() != null ? hc.getTalla().doubleValue() : null)
                .temperatura(hc.getTemperatura() != null ? hc.getTemperatura().doubleValue() : null)
                .diagnosticoGeneral(hc.getDiagnosticoGeneral())
                .observaciones(hc.getObservaciones())
                .recomendaciones(hc.getRecomendaciones())
                .fechaCreacion(hc.getFechaCreacion())
                .build();
    }
}

