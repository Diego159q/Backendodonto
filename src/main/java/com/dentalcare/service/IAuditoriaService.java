package com.dentalcare.service;

import com.dentalcare.dto.response.AuditoriaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuditoriaService {
    Page<AuditoriaResponse> listar(Pageable pageable);
    void registrar(String accion, String entidad, Long entidadId, String descripcion, Long usuarioId, String direccionIp);
}
