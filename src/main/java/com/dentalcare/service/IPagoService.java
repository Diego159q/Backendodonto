package com.dentalcare.service;

import com.dentalcare.dto.request.PagoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IPagoService {
    Page<PagoResponse> listar(Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    PagoResponse obtenerPorId(Long id);
    MensajeResponse crear(PagoRequest request, Long usuarioRegistroId);
    List<PagoResponse> listarDeudasPendientes();
    BigDecimal obtenerIngresosDelDia();
    BigDecimal obtenerIngresosDelMes();
    List<PagoResponse> obtenerPagosPorFecha(LocalDate fecha);
}
