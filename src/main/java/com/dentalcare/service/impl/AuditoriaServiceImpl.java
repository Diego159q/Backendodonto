package com.dentalcare.service.impl;

import com.dentalcare.dto.response.AuditoriaResponse;
import com.dentalcare.entity.Auditoria;
import com.dentalcare.entity.Usuario;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.AuditoriaRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.IAuditoriaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditoriaServiceImpl implements IAuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public AuditoriaServiceImpl(AuditoriaRepository auditoriaRepository,
                                UsuarioRepository usuarioRepository) {
        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> listar(Pageable pageable) {
        return auditoriaRepository.findAll(pageable)
                .map(this::toAuditoriaResponse);
    }

    @Override
    public void registrar(String accion, String entidad, Long entidadId, String descripcion,
                          Long usuarioId, String direccionIp) {
        Auditoria auditoria = new Auditoria();
        auditoria.setAccion(accion);
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setDescripcion(descripcion);
        auditoria.setDireccionIp(direccionIp);

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElse(null);
            auditoria.setUsuario(usuario);
        }

        auditoriaRepository.save(auditoria);
    }

    private AuditoriaResponse toAuditoriaResponse(Auditoria auditoria) {
        return AuditoriaResponse.builder()
                .id(auditoria.getId())
                .usuarioId(auditoria.getUsuario() != null ? auditoria.getUsuario().getId() : null)
                .usuarioNombre(auditoria.getUsuario() != null ?
                        auditoria.getUsuario().getNombres() + " " + auditoria.getUsuario().getApellidos() : null)
                .accion(auditoria.getAccion())
                .entidad(auditoria.getEntidad())
                .entidadId(auditoria.getEntidadId())
                .descripcion(auditoria.getDescripcion())
                .direccionIp(auditoria.getDireccionIp())
                .fecha(auditoria.getFecha())
                .datosAnteriores(auditoria.getDatosAnteriores())
                .datosNuevos(auditoria.getDatosNuevos())
                .build();
    }
}

