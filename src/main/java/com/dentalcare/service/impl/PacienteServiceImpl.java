package com.dentalcare.service.impl;

import com.dentalcare.dto.request.PacienteRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteResponse;
import com.dentalcare.entity.Paciente;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.service.IPacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PacienteServiceImpl implements IPacienteService {

    private final PacienteRepository pacienteRepository;
    private final MapperUtil mapperUtil;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, MapperUtil mapperUtil) {
        this.pacienteRepository = pacienteRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponse> listar(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            List<Paciente> filtered = pacienteRepository.combinedSearch(
                    search, search, search, search, search
            );
            List<PacienteResponse> dtos = filtered.stream()
                    .map(mapperUtil::toPacienteResponse)
                    .collect(Collectors.toList());
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), dtos.size());
            List<PacienteResponse> pageContent = start < dtos.size() ?
                    dtos.subList(start, end) : new ArrayList<>();
            return new PageImpl<>(pageContent, pageable, dtos.size());
        }
        return pacienteRepository.findAll(pageable).map(mapperUtil::toPacienteResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));
        return mapperUtil.toPacienteResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "dni", dni));
        return mapperUtil.toPacienteResponse(paciente);
    }

    @Override
    public MensajeResponse crear(PacienteRequest request) {
        if (pacienteRepository.findByDni(request.getDni()).isPresent()) {
            throw new DuplicateResourceException("Paciente", "dni", request.getDni());
        }

        String codigoPaciente = generarCodigoPaciente();

        Paciente paciente = new Paciente();
        paciente.setCodigoPaciente(codigoPaciente);
        paciente.setNombres(request.getNombres());
        paciente.setApellidos(request.getApellidos());
        paciente.setDni(request.getDni());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setSexo(request.getSexo());
        paciente.setTelefono(request.getTelefono());
        paciente.setEmail(request.getEmail());
        paciente.setDireccion(request.getDireccion());
        paciente.setDistrito(request.getDistrito());
        paciente.setCiudad(request.getCiudad());
        paciente.setEstadoCivil(request.getEstadoCivil());
        paciente.setOcupacion(request.getOcupacion());
        paciente.setTipoSangre(request.getTipoSangre());
        paciente.setAlergias(request.getAlergias());
        paciente.setEnfermedadesPrevias(request.getEnfermedadesPrevias());
        paciente.setMedicamentosActuales(request.getMedicamentosActuales());
        paciente.setContactoEmergencia(request.getContactoEmergencia());
        paciente.setTelefonoEmergencia(request.getTelefonoEmergencia());
        paciente.setObservaciones(request.getObservaciones());
        paciente.setActivo(true);
        paciente.setFechaRegistro(LocalDateTime.now());

        pacienteRepository.save(paciente);

        return MensajeResponse.builder()
                .mensaje("Paciente creado exitosamente. C\u00f3digo: " + codigoPaciente)
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse actualizar(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));

        if (!paciente.getDni().equals(request.getDni()) &&
                pacienteRepository.findByDni(request.getDni()).isPresent()) {
            throw new DuplicateResourceException("Paciente", "dni", request.getDni());
        }

        paciente.setNombres(request.getNombres());
        paciente.setApellidos(request.getApellidos());
        paciente.setDni(request.getDni());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setSexo(request.getSexo());
        paciente.setTelefono(request.getTelefono());
        paciente.setEmail(request.getEmail());
        paciente.setDireccion(request.getDireccion());
        paciente.setDistrito(request.getDistrito());
        paciente.setCiudad(request.getCiudad());
        paciente.setEstadoCivil(request.getEstadoCivil());
        paciente.setOcupacion(request.getOcupacion());
        paciente.setTipoSangre(request.getTipoSangre());
        paciente.setAlergias(request.getAlergias());
        paciente.setEnfermedadesPrevias(request.getEnfermedadesPrevias());
        paciente.setMedicamentosActuales(request.getMedicamentosActuales());
        paciente.setContactoEmergencia(request.getContactoEmergencia());
        paciente.setTelefonoEmergencia(request.getTelefonoEmergencia());
        paciente.setObservaciones(request.getObservaciones());

        pacienteRepository.save(paciente);

        return MensajeResponse.builder()
                .mensaje("Paciente actualizado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse eliminar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", id));

        paciente.setActivo(false);
        pacienteRepository.save(paciente);

        return MensajeResponse.builder()
                .mensaje("Paciente eliminado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscar(String termino) {
        List<Paciente> resultados = new ArrayList<>();
        resultados.addAll(pacienteRepository.findByDniContaining(termino));
        resultados.addAll(pacienteRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                termino, termino));
        return resultados.stream()
                .distinct()
                .map(mapperUtil::toPacienteResponse)
                .collect(Collectors.toList());
    }

    private String generarCodigoPaciente() {
        return pacienteRepository.findTopByOrderByIdDesc()
                .map(p -> {
                    String lastCode = p.getCodigoPaciente();
                    if (lastCode != null && lastCode.startsWith("PAC-")) {
                        int lastNum = Integer.parseInt(lastCode.substring(4));
                        return "PAC-" + String.format("%05d", lastNum + 1);
                    }
                    return "PAC-00001";
                })
                .orElse("PAC-00001");
    }
}

