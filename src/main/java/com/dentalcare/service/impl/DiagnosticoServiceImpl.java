package com.dentalcare.service.impl;

import com.dentalcare.dto.request.DiagnosticoRequest;
import com.dentalcare.dto.response.DiagnosticoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.Diagnostico;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.DiagnosticoRepository;
import com.dentalcare.service.IDiagnosticoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosticoServiceImpl implements IDiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;

    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> listar() {
        return diagnosticoRepository.findAll().stream()
                .map(this::toDiagnosticoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticoResponse obtenerPorId(Long id) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnostico", "id", id));
        return toDiagnosticoResponse(diagnostico);
    }

    @Override
    public MensajeResponse crear(DiagnosticoRequest request) {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setCodigo(request.getCodigo());
        diagnostico.setNombre(request.getNombre());
        diagnostico.setDescripcion(request.getDescripcion());
        diagnostico.setActivo(request.getActivo() != null ? request.getActivo() : true);

        diagnosticoRepository.save(diagnostico);

        return MensajeResponse.builder()
                .mensaje("Diagn\u00f3stico creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, DiagnosticoRequest request) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnostico", "id", id));

        diagnostico.setCodigo(request.getCodigo());
        diagnostico.setNombre(request.getNombre());
        diagnostico.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) {
            diagnostico.setActivo(request.getActivo());
        }

        diagnosticoRepository.save(diagnostico);

        return MensajeResponse.builder()
                .mensaje("Diagn\u00f3stico actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private DiagnosticoResponse toDiagnosticoResponse(Diagnostico diagnostico) {
        return DiagnosticoResponse.builder()
                .id(diagnostico.getId())
                .codigo(diagnostico.getCodigo())
                .nombre(diagnostico.getNombre())
                .descripcion(diagnostico.getDescripcion())
                .activo(diagnostico.getActivo())
                .fechaCreacion(diagnostico.getFechaCreacion())
                .build();
    }
}

