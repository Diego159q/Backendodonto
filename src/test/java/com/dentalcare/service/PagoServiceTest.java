package com.dentalcare.service;

import com.dentalcare.dto.request.PagoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.Paciente;
import com.dentalcare.entity.Pago;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.repository.PagoRepository;
import com.dentalcare.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MapperUtil mapperUtil;

    @Mock
    private com.dentalcare.repository.UsuarioRepository usuarioRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private PagoRequest request;
    private Paciente paciente;
    private Pago pago;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombres("Juan");
        paciente.setApellidos("Perez");

        request = new PagoRequest();
        request.setPacienteId(1L);
        request.setMonto(new BigDecimal("150.00"));
        request.setMetodoPago("EFECTIVO");

        pago = new Pago();
        pago.setId(1L);
        pago.setNumeroPago("PAG-00001");
        pago.setPaciente(paciente);
        pago.setMonto(new BigDecimal("150.00"));
        pago.setMetodoPago(com.dentalcare.entity.MetodoPago.EFECTIVO);
        pago.setEstado(com.dentalcare.entity.EstadoPago.PAGADO);
    }

    @Test
    void testCrearPago_Success() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new com.dentalcare.entity.Usuario()));
        when(pagoRepository.findTopByOrderByNumeroPagoDesc()).thenReturn(Optional.empty());
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        MensajeResponse result = pagoService.crear(request, 1L);

        assertTrue(result.isSuccess());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    @org.junit.jupiter.api.Disabled("Validacion de monto negativo fue movida a @Positive en PagoRequest")
    void testCrearPago_NegativeAmount() {
        request.setMonto(new BigDecimal("-50.00"));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new com.dentalcare.entity.Usuario()));

        assertThrows(BadRequestException.class, () -> pagoService.crear(request, 1L));
    }

    @Test
    void testIngresosDelDia() {
        when(pagoRepository.sumMontosByFechaBetween(any(), any()))
                .thenReturn(new BigDecimal("500.00"));

        BigDecimal ingresos = pagoService.obtenerIngresosDelDia();

        assertEquals(new BigDecimal("500.00"), ingresos);
    }
}
