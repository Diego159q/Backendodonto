package com.dentalcare.service.impl;

import com.dentalcare.dto.response.DashboardResponse;
import com.dentalcare.entity.EstadoTratamiento;
import com.dentalcare.entity.Paciente;
import com.dentalcare.repository.*;
import com.dentalcare.service.IDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DashboardServiceImpl implements IDashboardService {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;
    private final PacienteTratamientoRepository pacienteTratamientoRepository;
    private final CuotaRepository cuotaRepository;
    private final ProductoRepository productoRepository;

    public DashboardServiceImpl(PacienteRepository pacienteRepository,
                                CitaRepository citaRepository,
                                PagoRepository pagoRepository,
                                PacienteTratamientoRepository pacienteTratamientoRepository,
                                CuotaRepository cuotaRepository,
                                ProductoRepository productoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.pagoRepository = pagoRepository;
        this.pacienteTratamientoRepository = pacienteTratamientoRepository;
        this.cuotaRepository = cuotaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtenerDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        long totalPacientes = pacienteRepository.count();
        List<Paciente> pacientesDelMes = pacienteRepository.findAll().stream()
                .filter(p -> p.getFechaCreacion() != null &&
                        !p.getFechaCreacion().toLocalDate().isBefore(startOfMonth) &&
                        !p.getFechaCreacion().toLocalDate().isAfter(endOfMonth))
                .collect(java.util.stream.Collectors.toList());
        long pacientesNuevosMes = pacientesDelMes.size();

        long citasDelDia = citaRepository.countByFecha(today);
        long citasPendientes = citaRepository.countByEstadoAndFechaBetween("PENDIENTE", today, endOfMonth);
        long citasConfirmadas = citaRepository.countByEstadoAndFechaBetween("CONFIRMADA", today, endOfMonth);
        long citasAtendidas = citaRepository.countByEstadoAndFechaBetween("ATENDIDA", today, endOfMonth);
        long citasCanceladas = citaRepository.countByEstadoAndFechaBetween("CANCELADA", today, endOfMonth);

        BigDecimal ingresosDelDia = pagoRepository.sumMontosByFechaBetween(today, today);
        if (ingresosDelDia == null) ingresosDelDia = BigDecimal.ZERO;

        BigDecimal ingresosDelMes = pagoRepository.sumMontosByFechaBetween(startOfMonth, endOfMonth);
        if (ingresosDelMes == null) ingresosDelMes = BigDecimal.ZERO;

        long tratamientosPendientes = pacienteTratamientoRepository.countByEstado(EstadoTratamiento.PENDIENTE);
        long tratamientosEnProceso = pacienteTratamientoRepository.countByEstado(EstadoTratamiento.EN_PROCESO);
        long tratamientosTerminados = pacienteTratamientoRepository.countByEstado(EstadoTratamiento.TERMINADO);

        long cuotasPendientes = cuotaRepository.findByEstado("PENDIENTE").size();

        long productosStockBajo = productoRepository.findByStockActualLessThanEqual(5).size();
        long productosProximosVencer = productoRepository
                .findByFechaVencimientoBetween(today, today.plusDays(30)).size();

        return DashboardResponse.builder()
                .totalPacientes(totalPacientes)
                .pacientesNuevosMes(pacientesNuevosMes)
                .citasDelDia(citasDelDia)
                .citasPendientes(citasPendientes)
                .citasConfirmadas(citasConfirmadas)
                .citasAtendidas(citasAtendidas)
                .citasCanceladas(citasCanceladas)
                .ingresosDelDia(ingresosDelDia)
                .ingresosDelMes(ingresosDelMes)
                .tratamientosPendientes(tratamientosPendientes)
                .tratamientosEnProceso(tratamientosEnProceso)
                .tratamientosTerminados(tratamientosTerminados)
                .cuotasPendientes(cuotasPendientes)
                .productosStockBajo(productosStockBajo)
                .productosProximosVencer(productosProximosVencer)
                .build();
    }
}

