package com.dentalcare.service;

import com.dentalcare.dto.request.ProductoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.ProductoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {
    Page<ProductoResponse> listar(String search, Long categoriaId, Pageable pageable);
    ProductoResponse obtenerPorId(Long id);
    MensajeResponse crear(ProductoRequest request);
    MensajeResponse actualizar(Long id, ProductoRequest request);
    List<ProductoResponse> listarStockBajo();
    List<ProductoResponse> listarProximosVencer(Integer dias);
}
