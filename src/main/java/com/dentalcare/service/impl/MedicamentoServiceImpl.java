package com.dentalcare.service.impl;

import com.dentalcare.dto.request.MedicamentoRequest;
import com.dentalcare.dto.response.MedicamentoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.Medicamento;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.MedicamentoRepository;
import com.dentalcare.service.IMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicamentoServiceImpl implements IMedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentoResponse> listar() {
        return medicamentoRepository.findAll().stream()
                .map(this::toMedicamentoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse crear(MedicamentoRequest request) {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre(request.getNombre());
        medicamento.setPresentacion(request.getPresentacion());
        medicamento.setConcentracion(request.getConcentracion());
        medicamento.setDescripcion(request.getDescripcion());
        medicamento.setActivo(request.getActivo() != null ? request.getActivo() : true);

        medicamentoRepository.save(medicamento);

        return MensajeResponse.builder()
                .mensaje("Medicamento creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, MedicamentoRequest request) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento", "id", id));

        medicamento.setNombre(request.getNombre());
        medicamento.setPresentacion(request.getPresentacion());
        medicamento.setConcentracion(request.getConcentracion());
        medicamento.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) {
            medicamento.setActivo(request.getActivo());
        }

        medicamentoRepository.save(medicamento);

        return MensajeResponse.builder()
                .mensaje("Medicamento actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private MedicamentoResponse toMedicamentoResponse(Medicamento medicamento) {
        return MedicamentoResponse.builder()
                .id(medicamento.getId())
                .nombre(medicamento.getNombre())
                .presentacion(medicamento.getPresentacion())
                .concentracion(medicamento.getConcentracion())
                .descripcion(medicamento.getDescripcion())
                .activo(medicamento.getActivo())
                .build();
    }
}

