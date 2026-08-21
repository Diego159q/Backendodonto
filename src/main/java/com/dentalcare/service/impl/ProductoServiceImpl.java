package com.dentalcare.service.impl;

import com.dentalcare.dto.request.ProductoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.ProductoResponse;
import com.dentalcare.entity.CategoriaProducto;
import com.dentalcare.entity.Producto;
import com.dentalcare.entity.Proveedor;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.CategoriaProductoRepository;
import com.dentalcare.repository.ProductoRepository;
import com.dentalcare.repository.ProveedorRepository;
import com.dentalcare.service.IProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaProductoRepository;
    private final ProveedorRepository proveedorRepository;
    private final MapperUtil mapperUtil;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               CategoriaProductoRepository categoriaProductoRepository,
                               ProveedorRepository proveedorRepository,
                               MapperUtil mapperUtil) {
        this.productoRepository = productoRepository;
        this.categoriaProductoRepository = categoriaProductoRepository;
        this.proveedorRepository = proveedorRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponse> listar(String search, Long categoriaId, Pageable pageable) {
        if (categoriaId != null) {
            List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);
            return toPage(productos, pageable);
        }

        if (search != null && !search.trim().isEmpty()) {
            List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(search);
            return toPage(productos, pageable);
        }

        return productoRepository.findAll(pageable).map(mapperUtil::toProductoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        return mapperUtil.toProductoResponse(producto);
    }

    @Override
    public MensajeResponse crear(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setStockActual(request.getStockActual() != null ? request.getStockActual() : 0);
        producto.setStockMinimo(request.getStockMinimo() != null ? request.getStockMinimo() : 5);
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setFechaVencimiento(request.getFechaVencimiento());
        producto.setLote(request.getLote());
        producto.setActivo(request.getActivo() != null ? request.getActivo() : true);

        if (request.getCategoriaId() != null) {
            CategoriaProducto categoria = categoriaProductoRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", request.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        if (request.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", request.getProveedorId()));
            producto.setProveedor(proveedor);
        }

        productoRepository.save(producto);

        return MensajeResponse.builder()
                .mensaje("Producto creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setFechaVencimiento(request.getFechaVencimiento());
        producto.setLote(request.getLote());

        if (request.getStockActual() != null) producto.setStockActual(request.getStockActual());
        if (request.getStockMinimo() != null) producto.setStockMinimo(request.getStockMinimo());
        if (request.getActivo() != null) producto.setActivo(request.getActivo());

        if (request.getCategoriaId() != null) {
            CategoriaProducto categoria = categoriaProductoRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", request.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        if (request.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", request.getProveedorId()));
            producto.setProveedor(proveedor);
        }

        productoRepository.save(producto);

        return MensajeResponse.builder()
                .mensaje("Producto actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarStockBajo() {
        return productoRepository.findByStockActualLessThanEqual(5).stream()
                .map(mapperUtil::toProductoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProximosVencer(Integer dias) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(dias);
        return productoRepository.findByFechaVencimientoBetween(start, end).stream()
                .map(mapperUtil::toProductoResponse)
                .collect(Collectors.toList());
    }

    private Page<ProductoResponse> toPage(List<Producto> productos, Pageable pageable) {
        List<ProductoResponse> dtos = productos.stream()
                .map(mapperUtil::toProductoResponse)
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<ProductoResponse> pageContent = start < dtos.size() ? dtos.subList(start, end) : List.of();
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }
}

