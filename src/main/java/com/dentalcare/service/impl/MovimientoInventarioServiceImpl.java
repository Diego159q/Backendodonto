package com.dentalcare.service.impl;

import com.dentalcare.dto.request.MovimientoInventarioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.MovimientoInventarioResponse;
import com.dentalcare.entity.MovimientoInventario;
import com.dentalcare.entity.Producto;
import com.dentalcare.entity.TipoMovimiento;
import com.dentalcare.entity.Usuario;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.MovimientoInventarioRepository;
import com.dentalcare.repository.ProductoRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.IMovimientoInventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoInventarioServiceImpl implements IMovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoInventarioServiceImpl(MovimientoInventarioRepository movimientoInventarioRepository,
                                           ProductoRepository productoRepository,
                                           UsuarioRepository usuarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listarPorProducto(Long productoId) {
        return movimientoInventarioRepository.findByProductoIdOrderByFechaDesc(productoId)
                .stream()
                .map(this::toMovimientoInventarioResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse registrar(MovimientoInventarioRequest request, Long usuarioId) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", request.getProductoId()));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        TipoMovimiento tipo = TipoMovimiento.valueOf(request.getTipoMovimiento().toUpperCase());
        Integer stockAnterior = producto.getStockActual();
        Integer stockNuevo;

        if (tipo == TipoMovimiento.ENTRADA) {
            stockNuevo = stockAnterior + request.getCantidad();
        } else if (tipo == TipoMovimiento.SALIDA) {
            if (stockAnterior < request.getCantidad()) {
                throw new BadRequestException("Stock insuficiente. Stock actual: " + stockAnterior);
            }
            stockNuevo = stockAnterior - request.getCantidad();
        } else {
            throw new BadRequestException("Tipo de movimiento no v\u00e1lido: " + request.getTipoMovimiento());
        }

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipo);
        movimiento.setCantidad(request.getCantidad());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setMotivo(request.getMotivo());
        movimiento.setUsuarioRegistro(usuario);

        movimientoInventarioRepository.save(movimiento);

        return MensajeResponse.builder()
                .mensaje("Movimiento de inventario registrado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private MovimientoInventarioResponse toMovimientoInventarioResponse(MovimientoInventario m) {
        return MovimientoInventarioResponse.builder()
                .id(m.getId())
                .productoId(m.getProducto() != null ? m.getProducto().getId() : null)
                .productoNombre(m.getProducto() != null ? m.getProducto().getNombre() : null)
                .tipoMovimiento(m.getTipoMovimiento() != null ? m.getTipoMovimiento().name() : null)
                .cantidad(m.getCantidad())
                .stockAnterior(m.getStockAnterior())
                .stockNuevo(m.getStockNuevo())
                .motivo(m.getMotivo())
                .fecha(m.getFecha())
                .usuarioRegistroId(m.getUsuarioRegistro() != null ? m.getUsuarioRegistro().getId() : null)
                .usuarioRegistroNombre(m.getUsuarioRegistro() != null ?
                        m.getUsuarioRegistro().getNombres() + " " + m.getUsuarioRegistro().getApellidos() : null)
                .build();
    }
}

