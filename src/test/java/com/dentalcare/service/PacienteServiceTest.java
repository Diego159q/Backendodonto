package com.dentalcare.service;

import com.dentalcare.dto.request.PacienteRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PacienteResponse;
import com.dentalcare.entity.Paciente;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    private Paciente paciente;
    private PacienteRequest request;
    private PacienteResponse response;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setCodigoPaciente("PAC-00001");
        paciente.setNombres("Juan");
        paciente.setApellidos("Perez");
        paciente.setDni("12345678");
        paciente.setActivo(true);

        request = new PacienteRequest();
        request.setNombres("Juan");
        request.setApellidos("Perez");
        request.setDni("12345678");

        response = new PacienteResponse();
        response.setId(1L);
        response.setCodigoPaciente("PAC-00001");
        response.setNombres("Juan");
        response.setApellidos("Perez");
        response.setDni("12345678");
    }

    @Test
    void testCrearPaciente_Success() {
        when(pacienteRepository.findByDni("12345678")).thenReturn(Optional.empty());
        when(pacienteRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        MensajeResponse result = pacienteService.crear(request);

        assertTrue(result.isSuccess());
        assertEquals("Paciente creado exitosamente. Código: PAC-00001", result.getMensaje());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void testCrearPaciente_DuplicateDni() {
        when(pacienteRepository.findByDni("12345678")).thenReturn(Optional.of(paciente));

        assertThrows(DuplicateResourceException.class, () -> {
            pacienteService.crear(request);
        });
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void testBuscarPacientePorId() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(mapperUtil.toPacienteResponse(paciente)).thenReturn(response);

        PacienteResponse result = pacienteService.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getNombres());
    }

    @Test
    void testListarPacientes() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Paciente> pacientesList = List.of(paciente);
        when(pacienteRepository.combinedSearch(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(pacientesList);
        when(mapperUtil.toPacienteResponse(paciente)).thenReturn(response);

        Page<PacienteResponse> result = pacienteService.listar("Juan", pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }
}
