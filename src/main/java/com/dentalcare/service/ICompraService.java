package com.dentalcare.service;

import com.dentalcare.dto.request.CompraRequest;
import com.dentalcare.dto.response.CompraResponse;
import com.dentalcare.dto.response.MensajeResponse;

import java.util.List;

public interface ICompraService {
    List<CompraResponse> listar();
    CompraResponse obtenerPorId(Long id);
    MensajeResponse crear(CompraRequest request, Long usuarioId);
}
