package com.dentalcare.service.impl;

import com.dentalcare.dto.request.TratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.TratamientoResponse;
import com.dentalcare.entity.Tratamiento;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.TratamientoRepository;
import com.dentalcare.service.ITratamientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TratamientoServiceImpl implements ITratamientoService {

    private final TratamientoRepository tratamientoRepository;

    public TratamientoServiceImpl(TratamientoRepository tratamientoRepository) {
        this.tratamientoRepository = tratamientoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TratamientoResponse> listar() {
        return tratamientoRepository.findAll().stream()
                .map(this::toTratamientoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TratamientoResponse obtenerPorId(Long id) {
        Tratamiento tratamiento = tratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento", "id", id));
        return toTratamientoResponse(tratamiento);
    }

    @Override
    public MensajeResponse crear(TratamientoRequest request) {
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setNombre(request.getNombre());
        tratamiento.setDescripcion(request.getDescripcion());
        tratamiento.setPrecioBase(request.getPrecioBase());
        tratamiento.setNumeroSesiones(request.getNumeroSesiones());
        tratamiento.setActivo(request.getActivo() != null ? request.getActivo() : true);

        tratamientoRepository.save(tratamiento);

        return MensajeResponse.builder()
                .mensaje("Tratamiento creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, TratamientoRequest request) {
        Tratamiento tratamiento = tratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento", "id", id));

        tratamiento.setNombre(request.getNombre());
        tratamiento.setDescripcion(request.getDescripcion());
        tratamiento.setPrecioBase(request.getPrecioBase());
        tratamiento.setNumeroSesiones(request.getNumeroSesiones());
        if (request.getActivo() != null) {
            tratamiento.setActivo(request.getActivo());
        }

        tratamientoRepository.save(tratamiento);

        return MensajeResponse.builder()
                .mensaje("Tratamiento actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private TratamientoResponse toTratamientoResponse(Tratamiento tratamiento) {
        return TratamientoResponse.builder()
                .id(tratamiento.getId())
                .nombre(tratamiento.getNombre())
                .descripcion(tratamiento.getDescripcion())
                .precioBase(tratamiento.getPrecioBase())
                .numeroSesiones(tratamiento.getNumeroSesiones())
                .activo(tratamiento.getActivo())
                .fechaCreacion(tratamiento.getFechaCreacion())
                .build();
    }
}

