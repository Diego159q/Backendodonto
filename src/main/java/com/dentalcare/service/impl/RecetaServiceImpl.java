package com.dentalcare.service.impl;

import com.dentalcare.dto.request.RecetaDetalleRequest;
import com.dentalcare.dto.request.RecetaRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.RecetaResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.*;
import com.dentalcare.service.IRecetaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecetaServiceImpl implements IRecetaService {

    private final RecetaRepository recetaRepository;
    private final RecetaDetalleRepository recetaDetalleRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final MapperUtil mapperUtil;

    public RecetaServiceImpl(RecetaRepository recetaRepository,
                             RecetaDetalleRepository recetaDetalleRepository,
                             PacienteRepository pacienteRepository,
                             OdontologoRepository odontologoRepository,
                             MedicamentoRepository medicamentoRepository,
                             MapperUtil mapperUtil) {
        this.recetaRepository = recetaRepository;
        this.recetaDetalleRepository = recetaDetalleRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponse> listar(Long pacienteId) {
        List<Receta> recetas;
        if (pacienteId != null) {
            recetas = recetaRepository.findByPacienteId(pacienteId);
        } else {
            recetas = recetaRepository.findAll();
        }
        return mapperUtil.toRecetaResponseList(recetas);
    }

    @Override
    @Transactional(readOnly = true)
    public RecetaResponse obtenerPorId(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", "id", id));
        return mapperUtil.toRecetaResponse(receta);
    }

    @Override
    public MensajeResponse crear(RecetaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        Receta receta = new Receta();
        receta.setPaciente(paciente);
        receta.setOdontologo(odontologo);
        receta.setDiagnostico(request.getDiagnostico());
        receta.setFecha(LocalDate.now());
        receta.setObservaciones(request.getObservaciones());
        receta.setAprobada(false);
        receta.setActivo(true);

        recetaRepository.save(receta);

        if (request.getMedicamentos() != null) {
            for (RecetaDetalleRequest detReq : request.getMedicamentos()) {
                Medicamento medicamento = medicamentoRepository.findById(detReq.getMedicamentoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Medicamento", "id",
                                detReq.getMedicamentoId()));

                RecetaDetalle detalle = new RecetaDetalle();
                detalle.setReceta(receta);
                detalle.setMedicamento(medicamento);
                detalle.setDosis(detReq.getDosis());
                detalle.setFrecuencia(detReq.getFrecuencia());
                detalle.setDuracion(detReq.getDuracion());
                detalle.setIndicaciones(detReq.getIndicaciones());
                detalle.setOrden(detReq.getOrden() != null ? detReq.getOrden() : 0);

                recetaDetalleRepository.save(detalle);
            }
        }

        return MensajeResponse.builder()
                .mensaje("Receta creada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse aprobar(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", "id", id));

        receta.setAprobada(true);
        receta.setFechaAprobacion(LocalDateTime.now());
        recetaRepository.save(receta);

        return MensajeResponse.builder()
                .mensaje("Receta aprobada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse anular(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", "id", id));

        receta.setActivo(false);
        recetaRepository.save(receta);

        return MensajeResponse.builder()
                .mensaje("Receta anulada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
