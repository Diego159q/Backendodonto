package com.dentalcare.service.impl;

import com.dentalcare.dto.request.ProveedorRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.ProveedorResponse;
import com.dentalcare.entity.Proveedor;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.ProveedorRepository;
import com.dentalcare.service.IProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProveedorServiceImpl implements IProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponse> listar() {
        return proveedorRepository.findAll().stream()
                .map(this::toProveedorResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponse obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", id));
        return toProveedorResponse(proveedor);
    }

    @Override
    public MensajeResponse crear(ProveedorRequest request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setRuc(request.getRuc());
        proveedor.setContacto(request.getContacto());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setActivo(request.getActivo() != null ? request.getActivo() : true);

        proveedorRepository.save(proveedor);

        return MensajeResponse.builder()
                .mensaje("Proveedor creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, ProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", id));

        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setRuc(request.getRuc());
        proveedor.setContacto(request.getContacto());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        proveedor.setDireccion(request.getDireccion());
        if (request.getActivo() != null) {
            proveedor.setActivo(request.getActivo());
        }

        proveedorRepository.save(proveedor);

        return MensajeResponse.builder()
                .mensaje("Proveedor actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ProveedorResponse toProveedorResponse(Proveedor proveedor) {
        return ProveedorResponse.builder()
                .id(proveedor.getId())
                .razonSocial(proveedor.getRazonSocial())
                .ruc(proveedor.getRuc())
                .contacto(proveedor.getContacto())
                .telefono(proveedor.getTelefono())
                .email(proveedor.getEmail())
                .direccion(proveedor.getDireccion())
                .activo(proveedor.getActivo())
                .fechaCreacion(proveedor.getFechaCreacion())
                .build();
    }
}

