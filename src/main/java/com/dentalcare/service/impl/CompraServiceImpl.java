package com.dentalcare.service.impl;

import com.dentalcare.dto.request.CompraDetalleRequest;
import com.dentalcare.dto.request.CompraRequest;
import com.dentalcare.dto.response.CompraDetalleResponse;
import com.dentalcare.dto.response.CompraResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.*;
import com.dentalcare.service.ICompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompraServiceImpl implements ICompraService {

    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                             CompraDetalleRepository compraDetalleRepository,
                             ProveedorRepository proveedorRepository,
                             ProductoRepository productoRepository,
                             UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.compraDetalleRepository = compraDetalleRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponse> listar() {
        return compraRepository.findAll().stream()
                .map(this::toCompraResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompraResponse obtenerPorId(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra", "id", id));
        return toCompraResponse(compra);
    }

    @Override
    public MensajeResponse crear(CompraRequest request, Long usuarioId) {
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", request.getProveedorId()));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setFecha(request.getFecha() != null ? request.getFecha() : LocalDate.now());
        compra.setNumeroDocumento(request.getNumeroDocumento());
        compra.setEstado("CONFIRMADA");
        compra.setUsuarioRegistro(usuario);
        compra.setActivo(true);

        compraRepository.save(compra);

        BigDecimal montoTotal = BigDecimal.ZERO;

        if (request.getDetalles() != null) {
            for (CompraDetalleRequest detReq : request.getDetalles()) {
                Producto producto = productoRepository.findById(detReq.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Producto", "id",
                                detReq.getProductoId()));

                BigDecimal subtotal = detReq.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(detReq.getCantidad()));

                CompraDetalle detalle = new CompraDetalle();
                detalle.setCompra(compra);
                detalle.setProducto(producto);
                detalle.setCantidad(detReq.getCantidad());
                detalle.setPrecioUnitario(detReq.getPrecioUnitario());
                detalle.setSubtotal(subtotal);
                detalle.setLote(detReq.getLote());
                detalle.setFechaVencimiento(detReq.getFechaVencimiento());

                compraDetalleRepository.save(detalle);

                producto.setStockActual(producto.getStockActual() + detReq.getCantidad());
                if (detReq.getLote() != null) producto.setLote(detReq.getLote());
                if (detReq.getFechaVencimiento() != null)
                    producto.setFechaVencimiento(detReq.getFechaVencimiento());
                productoRepository.save(producto);

                montoTotal = montoTotal.add(subtotal);
            }
        }

        compra.setMontoTotal(montoTotal);
        compraRepository.save(compra);

        return MensajeResponse.builder()
                .mensaje("Compra registrada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private CompraResponse toCompraResponse(Compra compra) {
        return CompraResponse.builder()
                .id(compra.getId())
                .proveedorId(compra.getProveedor() != null ? compra.getProveedor().getId() : null)
                .proveedorNombre(compra.getProveedor() != null ? compra.getProveedor().getRazonSocial() : null)
                .fecha(compra.getFecha())
                .numeroDocumento(compra.getNumeroDocumento())
                .montoTotal(compra.getMontoTotal())
                .estado(compra.getEstado())
                .usuarioRegistroId(compra.getUsuarioRegistro() != null ? compra.getUsuarioRegistro().getId() : null)
                .usuarioRegistroNombre(compra.getUsuarioRegistro() != null ?
                        compra.getUsuarioRegistro().getNombres() + " " + compra.getUsuarioRegistro().getApellidos() : null)
                .fechaCreacion(compra.getFechaCreacion())
                .build();
    }
}
