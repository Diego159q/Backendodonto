package com.dentalcare.service.impl;

import com.dentalcare.dto.request.CuotaRequest;
import com.dentalcare.dto.response.CuotaResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.Cuota;
import com.dentalcare.entity.Pago;
import com.dentalcare.entity.PlanTratamiento;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.CuotaRepository;
import com.dentalcare.repository.PagoRepository;
import com.dentalcare.repository.PlanTratamientoRepository;
import com.dentalcare.service.ICuotaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuotaServiceImpl implements ICuotaService {

    private final CuotaRepository cuotaRepository;
    private final PlanTratamientoRepository planTratamientoRepository;
    private final PagoRepository pagoRepository;

    public CuotaServiceImpl(CuotaRepository cuotaRepository,
                            PlanTratamientoRepository planTratamientoRepository,
                            PagoRepository pagoRepository) {
        this.cuotaRepository = cuotaRepository;
        this.planTratamientoRepository = planTratamientoRepository;
        this.pagoRepository = pagoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuotaResponse> listarPorPlan(Long planTratamientoId) {
        return cuotaRepository.findByPlanTratamientoId(planTratamientoId)
                .stream()
                .map(this::toCuotaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse crear(CuotaRequest request) {
        PlanTratamiento plan = planTratamientoRepository.findById(request.getPlanTratamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("PlanTratamiento", "id", request.getPlanTratamientoId()));

        Cuota cuota = new Cuota();
        cuota.setPlanTratamiento(plan);
        cuota.setNumeroCuota(request.getNumeroCuota());
        cuota.setMonto(request.getMonto());
        cuota.setFechaVencimiento(request.getFechaVencimiento());
        cuota.setEstado("PENDIENTE");

        cuotaRepository.save(cuota);

        return MensajeResponse.builder()
                .mensaje("Cuota creada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse pagarCuota(Long cuotaId, Long pagoId) {
        Cuota cuota = cuotaRepository.findById(cuotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", cuotaId));

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", pagoId));

        cuota.setEstado("PAGADA");
        cuota.setFechaPago(LocalDate.now());
        cuota.setPagoRelacionado(pago);
        cuotaRepository.save(cuota);

        return MensajeResponse.builder()
                .mensaje("Cuota pagada exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuotaResponse> listarCuotasVencidas() {
        LocalDate today = LocalDate.now();
        return cuotaRepository.findByFechaVencimientoBeforeAndEstado(today, "PENDIENTE")
                .stream()
                .map(this::toCuotaResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public MensajeResponse actualizarCuotasVencidas() {
        LocalDate today = LocalDate.now();
        List<Cuota> vencidas = cuotaRepository.findByFechaVencimientoBeforeAndEstado(today, "PENDIENTE");
        for (Cuota cuota : vencidas) {
            cuota.setEstado("VENCIDA");
            cuotaRepository.save(cuota);
        }

        return MensajeResponse.builder()
                .mensaje(vencidas.size() + " cuotas marcadas como vencidas")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private CuotaResponse toCuotaResponse(Cuota cuota) {
        return CuotaResponse.builder()
                .id(cuota.getId())
                .planTratamientoId(cuota.getPlanTratamiento() != null ? cuota.getPlanTratamiento().getId() : null)
                .numeroCuota(cuota.getNumeroCuota())
                .monto(cuota.getMonto())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .fechaPago(cuota.getFechaPago())
                .estado(cuota.getEstado())
                .pagoRelacionadoId(cuota.getPagoRelacionado() != null ? cuota.getPagoRelacionado().getId() : null)
                .build();
    }
}

