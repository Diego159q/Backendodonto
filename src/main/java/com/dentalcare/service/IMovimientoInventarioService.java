package com.dentalcare.service;

import com.dentalcare.dto.request.MovimientoInventarioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.MovimientoInventarioResponse;

import java.util.List;

public interface IMovimientoInventarioService {
    List<MovimientoInventarioResponse> listarPorProducto(Long productoId);
    MensajeResponse registrar(MovimientoInventarioRequest request, Long usuarioId);
}
