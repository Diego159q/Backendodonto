package com.dentalcare.service;

import com.dentalcare.dto.request.UsuarioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {
    Page<UsuarioResponse> listar(Pageable pageable);
    UsuarioResponse obtenerPorId(Long id);
    UsuarioResponse obtenerPorEmail(String email);
    MensajeResponse crear(UsuarioRequest request);
    MensajeResponse actualizar(Long id, UsuarioRequest request);
    MensajeResponse cambiarEstado(Long id);
    MensajeResponse asignarRol(Long id, Long rolId);
}
