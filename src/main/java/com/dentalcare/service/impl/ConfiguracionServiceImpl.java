package com.dentalcare.service.impl;

import com.dentalcare.dto.request.ConfiguracionRequest;
import com.dentalcare.dto.response.ConfiguracionResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.ConfiguracionCentro;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.ConfiguracionCentroRepository;
import com.dentalcare.service.IConfiguracionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ConfiguracionServiceImpl implements IConfiguracionService {

    private final ConfiguracionCentroRepository configuracionCentroRepository;

    public ConfiguracionServiceImpl(ConfiguracionCentroRepository configuracionCentroRepository) {
        this.configuracionCentroRepository = configuracionCentroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ConfiguracionResponse obtenerConfiguracion() {
        List<ConfiguracionCentro> configs = configuracionCentroRepository.findAll();
        ConfiguracionCentro config = configs.isEmpty() ? null : configs.get(0);

        if (config == null) {
            throw new ResourceNotFoundException("Configuracion", "id", 1L);
        }

        return toConfiguracionResponse(config);
    }

    @Override
    public MensajeResponse actualizar(ConfiguracionRequest request) {
        List<ConfiguracionCentro> configs = configuracionCentroRepository.findAll();
        ConfiguracionCentro config = configs.isEmpty() ? new ConfiguracionCentro() : configs.get(0);

        if (request.getNombreCentro() != null) config.setNombreCentro(request.getNombreCentro());
        if (request.getRuc() != null) config.setRuc(request.getRuc());
        if (request.getDireccion() != null) config.setDireccion(request.getDireccion());
        if (request.getTelefono() != null) config.setTelefono(request.getTelefono());
        if (request.getEmail() != null) config.setEmail(request.getEmail());
        if (request.getLogoUrl() != null) config.setLogoUrl(request.getLogoUrl());
        if (request.getHorarioAtencion() != null) config.setHorarioAtencion(request.getHorarioAtencion());
        if (request.getDuracionCitaPredeterminada() != null)
            config.setDuracionCitaPredeterminada(request.getDuracionCitaPredeterminada());
        if (request.getMoneda() != null) config.setMoneda(request.getMoneda());
        if (request.getMensajeRecordatorio() != null) config.setMensajeRecordatorio(request.getMensajeRecordatorio());
        if (request.getNombreOdontologa() != null) config.setNombreOdontologa(request.getNombreOdontologa());
        if (request.getColegiatura() != null) config.setColegiatura(request.getColegiatura());
        if (request.getFirmaUrl() != null) config.setFirmaUrl(request.getFirmaUrl());

        configuracionCentroRepository.save(config);

        return MensajeResponse.builder()
                .mensaje("Configuraci\u00f3n actualizada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ConfiguracionResponse toConfiguracionResponse(ConfiguracionCentro config) {
        return ConfiguracionResponse.builder()
                .id(config.getId())
                .nombreCentro(config.getNombreCentro())
                .ruc(config.getRuc())
                .direccion(config.getDireccion())
                .telefono(config.getTelefono())
                .email(config.getEmail())
                .logoUrl(config.getLogoUrl())
                .horarioAtencion(config.getHorarioAtencion())
                .duracionCitaPredeterminada(config.getDuracionCitaPredeterminada())
                .moneda(config.getMoneda())
                .mensajeRecordatorio(config.getMensajeRecordatorio())
                .nombreOdontologa(config.getNombreOdontologa())
                .colegiatura(config.getColegiatura())
                .firmaUrl(config.getFirmaUrl())
                .build();
    }
}
