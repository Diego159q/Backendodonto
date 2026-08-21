package com.dentalcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {
    private long totalPacientes;
    private long pacientesNuevosMes;
    private long citasDelDia;
    private long citasPendientes;
    private long citasConfirmadas;
    private long citasAtendidas;
    private long citasCanceladas;
    private BigDecimal ingresosDelDia;
    private BigDecimal ingresosDelMes;
    private long tratamientosPendientes;
    private long tratamientosEnProceso;
    private long tratamientosTerminados;
    private long cuotasPendientes;
    private long productosStockBajo;
    private long productosProximosVencer;
}
