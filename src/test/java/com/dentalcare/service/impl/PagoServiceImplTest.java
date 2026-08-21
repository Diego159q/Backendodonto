package com.dentalcare.service.impl;

import com.dentalcare.dto.request.PagoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PlanTratamientoRepository planTratamientoRepository;
    @Mock
    private PacienteTratamientoRepository pacienteTratamientoRepository;
    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private PagoRequest pagoRequest;
    private Paciente paciente;
    private Usuario usuario;
    private PlanTratamiento planTratamiento;

    @BeforeEach
    void setUp() {
        pagoRequest = new PagoRequest();
        pagoRequest.setPacienteId(1L);
        pagoRequest.setPlanTratamientoId(1L);
        pagoRequest.setMonto(new BigDecimal("100.00"));
        pagoRequest.setMetodoPago("EFECTIVO");

        paciente = new Paciente();
        paciente.setId(1L);

        usuario = new Usuario();
        usuario.setId(1L);

        planTratamiento = new PlanTratamiento();
        planTratamiento.setId(1L);
        planTratamiento.setSaldo(new BigDecimal("150.00"));
        planTratamiento.setEstado("PENDIENTE");
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException cuando el monto del pago excede el saldo pendiente")
    void crear_MontoExcedeSaldo_LanzaException() {
        // Arrange
        pagoRequest.setMonto(new BigDecimal("200.00")); // Mayor al saldo (150.00)
        
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findTopByOrderByNumeroPagoDesc()).thenReturn(Optional.empty());
        when(planTratamientoRepository.findById(1L)).thenReturn(Optional.of(planTratamiento));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            pagoService.crear(pagoRequest, 1L);
        });

        assertEquals("El monto del pago excede el saldo pendiente", exception.getMessage());
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe marcar el plan como PAGADO cuando el pago deja el saldo exactamente en cero")
    void crear_PagoCompleto_CambiaEstadoAPagado() {
        // Arrange
        pagoRequest.setMonto(new BigDecimal("150.00")); // Exactamente igual al saldo
        
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findTopByOrderByNumeroPagoDesc()).thenReturn(Optional.empty());
        when(planTratamientoRepository.findById(1L)).thenReturn(Optional.of(planTratamiento));

        // Act
        MensajeResponse response = pagoService.crear(pagoRequest, 1L);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("PAGADO", planTratamiento.getEstado());
        assertEquals(0, BigDecimal.ZERO.compareTo(planTratamiento.getSaldo()));
        
        verify(planTratamientoRepository, times(1)).save(planTratamiento);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el plan de tratamiento no existe")
    void crear_PlanTratamientoNoExiste_LanzaException() {
        // Arrange
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findTopByOrderByNumeroPagoDesc()).thenReturn(Optional.empty());
        when(planTratamientoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pagoService.crear(pagoRequest, 1L);
        });

        assertEquals("PlanTratamiento no encontrado con id: '1'", exception.getMessage());
        verify(pagoRepository, never()).save(any(Pago.class));
    }
}
