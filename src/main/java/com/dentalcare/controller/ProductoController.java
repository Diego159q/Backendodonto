package com.dentalcare.controller;

import com.dentalcare.dto.request.ProductoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.ProductoResponse;
import com.dentalcare.service.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Productos", description = "Gesti\u00f3n de productos e inventario (solo ADMIN)")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Lista paginada de productos con b\u00fasqueda y filtro por categor\u00eda")
    public ResponseEntity<Page<ProductoResponse>> listar(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ProductoResponse> productos = productoService.listar(search, categoriaId, pageable);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene los detalles de un producto")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        ProductoResponse producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto")
    public ResponseEntity<MensajeResponse> crear(@Valid @RequestBody ProductoRequest request) {
        MensajeResponse response = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    public ResponseEntity<MensajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        MensajeResponse response = productoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Stock bajo", description = "Obtiene productos con stock por debajo del m\u00ednimo")
    public ResponseEntity<List<ProductoResponse>> listarStockBajo() {
        List<ProductoResponse> productos = productoService.listarStockBajo();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/proximos-vencer")
    @Operation(summary = "Pr\u00f3ximos a vencer", description = "Obtiene productos pr\u00f3ximos a vencer")
    public ResponseEntity<List<ProductoResponse>> listarProximosVencer(@RequestParam(defaultValue = "30") int dias) {
        List<ProductoResponse> productos = productoService.listarProximosVencer(dias);
        return ResponseEntity.ok(productos);
    }
}
