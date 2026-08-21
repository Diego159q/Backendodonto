package com.dentalcare.service;

import com.dentalcare.dto.request.CitaRequest;
import com.dentalcare.dto.response.CitaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.CitaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.service.impl.CitaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private OdontologoRepository odontologoRepository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private CitaServiceImpl citaService;

    private CitaRequest request;
    private Cita cita;
    private Paciente paciente;
    private Odontologo odontologo;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombres("Juan");
        paciente.setApellidos("Perez");

        odontologo = new Odontologo();
        odontologo.setId(1L);
        odontologo.setNombres("Maria");
        odontologo.setApellidos("Lopez");

        request = new CitaRequest();
        request.setPacienteId(1L);
        request.setOdontologoId(1L);
        request.setFecha(LocalDate.now().plusDays(1));
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFin(LocalTime.of(10, 30));
        request.setMotivo("Consulta general");

        cita = new Cita();
        cita.setId(1L);
        cita.setPaciente(paciente);
        cita.setOdontologo(odontologo);
        cita.setFecha(request.getFecha());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(request.getHoraFin());
        cita.setMotivo(request.getMotivo());
        cita.setEstado(com.dentalcare.entity.EstadoCita.PENDIENTE);
    }

    @Test
    void testCrearCita_Success() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(1L)).thenReturn(Optional.of(odontologo));
        when(citaRepository.findByFechaAndOdontologoIdOrderByHoraInicio(
                any(), anyLong())).thenReturn(Collections.emptyList());
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        MensajeResponse result = citaService.crear(request);

        assertTrue(result.isSuccess());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void testCrearCita_PastDate() {
        request.setFecha(LocalDate.now().minusDays(1));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(1L)).thenReturn(Optional.of(odontologo));

        assertThrows(BadRequestException.class, () -> citaService.crear(request));
    }

    @Test
    void testCrearCita_Overlapping() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(1L)).thenReturn(Optional.of(odontologo));
        when(citaRepository.findByFechaAndOdontologoIdOrderByHoraInicio(
                any(), anyLong())).thenReturn(Collections.singletonList(cita));

        assertThrows(BadRequestException.class, () -> citaService.crear(request));
    }

    @Test
    void testCrearCita_InvalidHours() {
        request.setHoraInicio(LocalTime.of(11, 0));
        request.setHoraFin(LocalTime.of(10, 30));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(odontologoRepository.findById(1L)).thenReturn(Optional.of(odontologo));

        assertThrows(BadRequestException.class, () -> citaService.crear(request));
    }

    @Test
    void testCancelarCita() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        MensajeResponse result = citaService.cancelar(1L, new com.dentalcare.dto.request.CancelarCitaRequest("Paciente cancelo"));

        assertTrue(result.isSuccess());
        assertEquals("Cita cancelada exitosamente", result.getMensaje());
    }
}
