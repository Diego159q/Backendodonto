package com.dentalcare.service.impl;

import com.dentalcare.dto.request.PagoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PagoResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.mapper.MapperUtil;
import com.dentalcare.repository.*;
import com.dentalcare.service.IPagoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoServiceImpl implements IPagoService {

    private final PagoRepository pagoRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanTratamientoRepository planTratamientoRepository;
    private final PacienteTratamientoRepository pacienteTratamientoRepository;
    private final MapperUtil mapperUtil;

    public PagoServiceImpl(PagoRepository pagoRepository,
                           PacienteRepository pacienteRepository,
                           UsuarioRepository usuarioRepository,
                           PlanTratamientoRepository planTratamientoRepository,
                           PacienteTratamientoRepository pacienteTratamientoRepository,
                           MapperUtil mapperUtil) {
        this.pagoRepository = pagoRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.planTratamientoRepository = planTratamientoRepository;
        this.pacienteTratamientoRepository = pacienteTratamientoRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> listar(Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        List<Pago> pagos;
        if (pacienteId != null) {
            pagos = pagoRepository.findByPacienteId(pacienteId);
        } else if (fechaInicio != null && fechaFin != null) {
            pagos = pagoRepository.findByFechaBetween(fechaInicio, fechaFin);
        } else {
            pagos = pagoRepository.findAll();
        }

        List<PagoResponse> dtos = mapperUtil.toPagoResponseList(pagos);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<PagoResponse> pageContent = start < dtos.size() ? dtos.subList(start, end) : List.of();
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));
        return mapperUtil.toPagoResponse(pago);
    }

    @Override
    public MensajeResponse crear(PagoRequest request, Long usuarioRegistroId) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Usuario usuarioRegistro = usuarioRepository.findById(usuarioRegistroId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioRegistroId));

        String numeroPago = generarNumeroPago();

        Pago pago = new Pago();
        pago.setNumeroPago(numeroPago);
        pago.setPaciente(paciente);
        pago.setUsuarioRegistro(usuarioRegistro);
        pago.setMonto(request.getMonto());
        pago.setFecha(request.getFecha() != null ? request.getFecha() : LocalDate.now());
        pago.setMetodoPago(MetodoPago.valueOf(request.getMetodoPago()));
        pago.setNumeroOperacion(request.getNumeroOperacion());
        pago.setObservaciones(request.getObservaciones());
        pago.setEstado(EstadoPago.PENDIENTE);

        if (request.getPlanTratamientoId() != null) {
            PlanTratamiento plan = planTratamientoRepository.findById(request.getPlanTratamientoId())
                    .orElseThrow(() -> new ResourceNotFoundException("PlanTratamiento", "id",
                            request.getPlanTratamientoId()));

            BigDecimal nuevoSaldo = plan.getSaldo().subtract(request.getMonto());
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("El monto del pago excede el saldo pendiente");
            }
            plan.setSaldo(nuevoSaldo);
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
                plan.setEstado("PAGADO");
            }
            planTratamientoRepository.save(plan);
            pago.setPlanTratamiento(plan);
        }

        pagoRepository.save(pago);

        return MensajeResponse.builder()
                .mensaje("Pago registrado exitosamente. N\u00famero: " + numeroPago)
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarDeudasPendientes() {
        return pagoRepository.findByEstado("PENDIENTE").stream()
                .map(mapperUtil::toPagoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerIngresosDelDia() {
        LocalDate today = LocalDate.now();
        BigDecimal total = pagoRepository.sumMontosByFechaBetween(today, today);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerIngresosDelMes() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        BigDecimal total = pagoRepository.sumMontosByFechaBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> obtenerPagosPorFecha(LocalDate fecha) {
        return pagoRepository.findByFechaBetween(fecha, fecha).stream()
                .map(mapperUtil::toPagoResponse)
                .collect(Collectors.toList());
    }

    private String generarNumeroPago() {
        return pagoRepository.findTopByOrderByNumeroPagoDesc()
                .map(p -> {
                    String lastNum = p.getNumeroPago().replace("PAG-", "");
                    int nextNum = Integer.parseInt(lastNum) + 1;
                    return "PAG-" + String.format("%05d", nextNum);
                })
                .orElse("PAG-00001");
    }
}
