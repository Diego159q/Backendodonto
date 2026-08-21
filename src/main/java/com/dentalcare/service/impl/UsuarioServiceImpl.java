package com.dentalcare.service.impl;

import com.dentalcare.dto.request.UsuarioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.UsuarioResponse;
import com.dentalcare.entity.Rol;
import com.dentalcare.entity.Usuario;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.RolRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.IUsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperUtil mapperUtil;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder,
                              MapperUtil mapperUtil) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public Page<UsuarioResponse> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(mapperUtil::toUsuarioResponse);
    }

    @Override
    public UsuarioResponse obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return mapperUtil.toUsuarioResponse(usuario);
    }

    @Override
    public UsuarioResponse obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
        return mapperUtil.toUsuarioResponse(usuario);
    }

    @Override
    public MensajeResponse crear(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", request.getEmail());
        }

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Usuario", "username", request.getUsername());
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setUsername(request.getUsername());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(
                request.getPassword() != null ? request.getPassword() : "dentalcare123"));
        usuario.setRol(rol);
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);
        usuario.setBloqueado(false);

        usuarioRepository.save(usuario);

        return MensajeResponse.builder()
                .mensaje("Usuario creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        if (!usuario.getEmail().equals(request.getEmail()) &&
                usuarioRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", request.getEmail());
        }

        if (!usuario.getUsername().equals(request.getUsername()) &&
                usuarioRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Usuario", "username", request.getUsername());
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getRolId()));

        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setUsername(request.getUsername());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(rol);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        }

        usuarioRepository.save(usuario);

        return MensajeResponse.builder()
                .mensaje("Usuario actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse cambiarEstado(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);

        String estado = usuario.getActivo() ? "activado" : "desactivado";
        return MensajeResponse.builder()
                .mensaje("Usuario " + estado + " exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse asignarRol(Long id, Long rolId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", rolId));

        usuario.setRol(rol);
        usuarioRepository.save(usuario);

        return MensajeResponse.builder()
                .mensaje("Rol asignado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

