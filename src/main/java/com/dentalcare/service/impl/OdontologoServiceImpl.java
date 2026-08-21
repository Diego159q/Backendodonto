package com.dentalcare.service.impl;

import com.dentalcare.dto.request.OdontologoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.OdontologoResponse;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.entity.Usuario;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.IOdontologoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OdontologoServiceImpl implements IOdontologoService {

    private final OdontologoRepository odontologoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MapperUtil mapperUtil;

    public OdontologoServiceImpl(OdontologoRepository odontologoRepository,
                                  UsuarioRepository usuarioRepository,
                                  MapperUtil mapperUtil) {
        this.odontologoRepository = odontologoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OdontologoResponse> listar() {
        return odontologoRepository.findAll().stream()
                .map(this::toOdontologoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OdontologoResponse obtenerPorId(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", id));
        return toOdontologoResponse(odontologo);
    }

    @Override
    public MensajeResponse crear(OdontologoRequest request) {
        if (odontologoRepository.findByNumeroColegiatura(request.getNumeroColegiatura()).isPresent()) {
            throw new DuplicateResourceException("Odontologo", "numeroColegiatura", request.getNumeroColegiatura());
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        Odontologo odontologo = new Odontologo();
        odontologo.setUsuario(usuario);
        odontologo.setNombres(request.getNombres());
        odontologo.setApellidos(request.getApellidos());
        odontologo.setDni(request.getDni());
        odontologo.setTelefono(request.getTelefono());
        odontologo.setEmail(request.getEmail());
        odontologo.setEspecialidad(request.getEspecialidad());
        odontologo.setNumeroColegiatura(request.getNumeroColegiatura());
        odontologo.setHorarioAtencion(request.getHorarioAtencion());
        odontologo.setFirmaUrl(request.getFirmaUrl());
        odontologo.setActivo(true);

        odontologoRepository.save(odontologo);

        return MensajeResponse.builder()
                .mensaje("Odont\u00f3logo creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, OdontologoRequest request) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", id));

        if (!odontologo.getNumeroColegiatura().equals(request.getNumeroColegiatura()) &&
                odontologoRepository.findByNumeroColegiatura(request.getNumeroColegiatura()).isPresent()) {
            throw new DuplicateResourceException("Odontologo", "numeroColegiatura", request.getNumeroColegiatura());
        }

        if (request.getUsuarioId() != null && !odontologo.getUsuario().getId().equals(request.getUsuarioId())) {
            Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));
            odontologo.setUsuario(usuario);
        }

        odontologo.setNombres(request.getNombres());
        odontologo.setApellidos(request.getApellidos());
        odontologo.setDni(request.getDni());
        odontologo.setTelefono(request.getTelefono());
        odontologo.setEmail(request.getEmail());
        odontologo.setEspecialidad(request.getEspecialidad());
        odontologo.setNumeroColegiatura(request.getNumeroColegiatura());
        odontologo.setHorarioAtencion(request.getHorarioAtencion());
        odontologo.setFirmaUrl(request.getFirmaUrl());

        odontologoRepository.save(odontologo);

        return MensajeResponse.builder()
                .mensaje("Odont\u00f3logo actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse cambiarEstado(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", id));

        odontologo.setActivo(!odontologo.getActivo());
        odontologoRepository.save(odontologo);

        String estado = odontologo.getActivo() ? "activado" : "desactivado";
        return MensajeResponse.builder()
                .mensaje("Odont\u00f3logo " + estado + " exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private OdontologoResponse toOdontologoResponse(Odontologo odontologo) {
        return OdontologoResponse.builder()
                .id(odontologo.getId())
                .usuarioId(odontologo.getUsuario() != null ? odontologo.getUsuario().getId() : null)
                .nombres(odontologo.getNombres())
                .apellidos(odontologo.getApellidos())
                .dni(odontologo.getDni())
                .telefono(odontologo.getTelefono())
                .email(odontologo.getEmail())
                .especialidad(odontologo.getEspecialidad())
                .numeroColegiatura(odontologo.getNumeroColegiatura())
                .horarioAtencion(odontologo.getHorarioAtencion())
                .firmaUrl(odontologo.getFirmaUrl())
                .activo(odontologo.getActivo())
                .fechaCreacion(odontologo.getFechaCreacion())
                .build();
    }
}

