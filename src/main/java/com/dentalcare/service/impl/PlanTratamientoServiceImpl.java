package com.dentalcare.service.impl;

import com.dentalcare.dto.request.PlanTratamientoDetalleRequest;
import com.dentalcare.dto.request.PlanTratamientoRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.PlanTratamientoDetalleResponse;
import com.dentalcare.dto.response.PlanTratamientoResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.*;
import com.dentalcare.service.IPlanTratamientoService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanTratamientoServiceImpl implements IPlanTratamientoService {

    private final PlanTratamientoRepository planTratamientoRepository;
    private final PlanTratamientoDetalleRepository planTratamientoDetalleRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final PacienteTratamientoRepository pacienteTratamientoRepository;

    public PlanTratamientoServiceImpl(PlanTratamientoRepository planTratamientoRepository,
                                      PlanTratamientoDetalleRepository planTratamientoDetalleRepository,
                                      PacienteRepository pacienteRepository,
                                      OdontologoRepository odontologoRepository,
                                      PacienteTratamientoRepository pacienteTratamientoRepository) {
        this.planTratamientoRepository = planTratamientoRepository;
        this.planTratamientoDetalleRepository = planTratamientoDetalleRepository;
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.pacienteTratamientoRepository = pacienteTratamientoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanTratamientoResponse> listarTodos() {
        return planTratamientoRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"))
                .stream()
                .map(this::toPlanTratamientoResponse)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<PlanTratamientoResponse> listarPorPaciente(Long pacienteId) {
        return planTratamientoRepository.findByPacienteIdOrderByFechaDesc(pacienteId)
                .stream()
                .map(this::toPlanTratamientoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlanTratamientoResponse obtenerPorId(Long id) {
        PlanTratamiento plan = planTratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTratamiento", "id", id));
        return toPlanTratamientoResponse(plan);
    }

    @Override
    public MensajeResponse crear(PlanTratamientoRequest request, List<PlanTratamientoDetalleRequest> detalles) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo", "id", request.getOdontologoId()));

        PlanTratamiento plan = new PlanTratamiento();
        plan.setPaciente(paciente);
        plan.setOdontologo(odontologo);
        plan.setFecha(LocalDate.now());
        plan.setDescuentoTotal(request.getDescuentoTotal() != null ? request.getDescuentoTotal() : BigDecimal.ZERO);
        plan.setAdelanto(request.getAdelanto() != null ? request.getAdelanto() : BigDecimal.ZERO);
        plan.setObservaciones(request.getObservaciones());
        plan.setEstado("PENDIENTE");
        plan.setAceptadoPorPaciente(false);
        plan.setActivo(true);

        planTratamientoRepository.save(plan);

        BigDecimal montoTotal = BigDecimal.ZERO;

        if (detalles != null) {
            for (PlanTratamientoDetalleRequest detReq : detalles) {
                PacienteTratamiento pt = pacienteTratamientoRepository.findById(detReq.getPacienteTratamientoId())
                        .orElseThrow(() -> new ResourceNotFoundException("PacienteTratamiento", "id",
                                detReq.getPacienteTratamientoId()));

                BigDecimal cantidad = BigDecimal.valueOf(detReq.getCantidad() != null ? detReq.getCantidad() : 1);
                BigDecimal precioUnitario = detReq.getPrecioUnitario() != null ? detReq.getPrecioUnitario() : BigDecimal.ZERO;
                BigDecimal descuentoDet = detReq.getDescuento() != null ? detReq.getDescuento() : BigDecimal.ZERO;
                BigDecimal subtotal = precioUnitario.multiply(cantidad).subtract(descuentoDet);

                PlanTratamientoDetalle detalle = new PlanTratamientoDetalle();
                detalle.setPlanTratamiento(plan);
                detalle.setTratamiento(pt);
                detalle.setPiezaDental(detReq.getPiezaDental() != null ?
                        Integer.parseInt(detReq.getPiezaDental()) : null);
                detalle.setCantidad(detReq.getCantidad() != null ? detReq.getCantidad() : 1);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setDescuento(descuentoDet);
                detalle.setSubtotal(subtotal);
                detalle.setEstado("PENDIENTE");
                detalle.setNumeroSesiones(detReq.getNumeroSesiones() != null ? detReq.getNumeroSesiones() : 1);

                planTratamientoDetalleRepository.save(detalle);
                montoTotal = montoTotal.add(subtotal);
            }
        }

        plan.setMontoTotal(montoTotal);
        BigDecimal descuentoTotal = request.getDescuentoTotal() != null ? request.getDescuentoTotal() : BigDecimal.ZERO;
        BigDecimal montoFinal = montoTotal.subtract(descuentoTotal);
        plan.setMontoFinal(montoFinal);

        BigDecimal adelanto = request.getAdelanto() != null ? request.getAdelanto() : BigDecimal.ZERO;
        plan.setSaldo(montoFinal.subtract(adelanto));

        planTratamientoRepository.save(plan);

        return MensajeResponse.builder()
                .mensaje("Plan de tratamiento creado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse aceptarPlan(Long id) {
        PlanTratamiento plan = planTratamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTratamiento", "id", id));

        plan.setAceptadoPorPaciente(true);
        plan.setFechaAceptacion(LocalDateTime.now());
        plan.setEstado("ACEPTADO");
        planTratamientoRepository.save(plan);

        return MensajeResponse.builder()
                .mensaje("Plan de tratamiento aceptado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse agregarDetalle(Long planId, PlanTratamientoDetalleRequest request) {
        PlanTratamiento plan = planTratamientoRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTratamiento", "id", planId));

        PacienteTratamiento pt = pacienteTratamientoRepository.findById(request.getPacienteTratamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("PacienteTratamiento", "id",
                        request.getPacienteTratamientoId()));

        BigDecimal cantidad = BigDecimal.valueOf(request.getCantidad() != null ? request.getCantidad() : 1);
        BigDecimal precioUnitario = request.getPrecioUnitario() != null ? request.getPrecioUnitario() : BigDecimal.ZERO;
        BigDecimal descuentoDet = request.getDescuento() != null ? request.getDescuento() : BigDecimal.ZERO;
        BigDecimal subtotal = precioUnitario.multiply(cantidad).subtract(descuentoDet);

        PlanTratamientoDetalle detalle = new PlanTratamientoDetalle();
        detalle.setPlanTratamiento(plan);
        detalle.setTratamiento(pt);
        detalle.setPiezaDental(request.getPiezaDental() != null ? Integer.parseInt(request.getPiezaDental()) : null);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setDescuento(descuentoDet);
        detalle.setSubtotal(subtotal);
        detalle.setEstado("PENDIENTE");
        detalle.setNumeroSesiones(request.getNumeroSesiones());

        planTratamientoDetalleRepository.save(detalle);

        recalcularTotales(plan);

        return MensajeResponse.builder()
                .mensaje("Detalle agregado al plan de tratamiento exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public MensajeResponse eliminarDetalle(Long detalleId) {
        PlanTratamientoDetalle detalle = planTratamientoDetalleRepository.findById(detalleId)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTratamientoDetalle", "id", detalleId));

        PlanTratamiento plan = detalle.getPlanTratamiento();
        planTratamientoDetalleRepository.delete(detalle);
        recalcularTotales(plan);

        return MensajeResponse.builder()
                .mensaje("Detalle eliminado del plan de tratamiento exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void recalcularTotales(PlanTratamiento plan) {
        List<PlanTratamientoDetalle> detalles = planTratamientoDetalleRepository.findByPlanId(plan.getId());
        BigDecimal montoTotal = detalles.stream()
                .map(d -> d.getSubtotal() != null ? d.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        plan.setMontoTotal(montoTotal);
        BigDecimal descuentoTotal = plan.getDescuentoTotal() != null ? plan.getDescuentoTotal() : BigDecimal.ZERO;
        BigDecimal montoFinal = montoTotal.subtract(descuentoTotal);
        plan.setMontoFinal(montoFinal);

        BigDecimal adelanto = plan.getAdelanto() != null ? plan.getAdelanto() : BigDecimal.ZERO;
        plan.setSaldo(montoFinal.subtract(adelanto));
        planTratamientoRepository.save(plan);
    }

    private PlanTratamientoResponse toPlanTratamientoResponse(PlanTratamiento plan) {
        List<PlanTratamientoDetalle> detalles = planTratamientoDetalleRepository.findByPlanId(plan.getId());

        return PlanTratamientoResponse.builder()
                .id(plan.getId())
                .pacienteId(plan.getPaciente() != null ? plan.getPaciente().getId() : null)
                .pacienteNombre(plan.getPaciente() != null ?
                        plan.getPaciente().getNombres() + " " + plan.getPaciente().getApellidos() : null)
                .odontologoId(plan.getOdontologo() != null ? plan.getOdontologo().getId() : null)
                .odontologoNombre(plan.getOdontologo() != null ?
                        plan.getOdontologo().getNombres() + " " + plan.getOdontologo().getApellidos() : null)
                .fecha(plan.getFecha())
                .montoTotal(plan.getMontoTotal())
                .descuentoTotal(plan.getDescuentoTotal())
                .montoFinal(plan.getMontoFinal())
                .adelanto(plan.getAdelanto())
                .saldo(plan.getSaldo())
                .estado(plan.getEstado())
                .aceptadoPorPaciente(plan.getAceptadoPorPaciente())
                .fechaAceptacion(plan.getFechaAceptacion() != null ?
                        plan.getFechaAceptacion().toLocalDate() : null)
                .observaciones(plan.getObservaciones())
                .detalles(detalles.stream().map(this::toDetalleResponse).collect(Collectors.toList()))
                .fechaCreacion(plan.getFechaCreacion())
                .build();
    }

    private PlanTratamientoDetalleResponse toDetalleResponse(PlanTratamientoDetalle detalle) {
        return PlanTratamientoDetalleResponse.builder()
                .id(detalle.getId())
                .planTratamientoId(detalle.getPlanTratamiento() != null ? detalle.getPlanTratamiento().getId() : null)
                .tratamientoId(detalle.getTratamiento() != null ? detalle.getTratamiento().getId() : null)
                .tratamientoNombre(detalle.getTratamiento() != null && detalle.getTratamiento().getTratamiento() != null ?
                        detalle.getTratamiento().getTratamiento().getNombre() : null)
                .piezaDental(detalle.getPiezaDental() != null ? String.valueOf(detalle.getPiezaDental()) : null)
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .descuento(detalle.getDescuento())
                .subtotal(detalle.getSubtotal())
                .estado(detalle.getEstado())
                .numeroSesiones(detalle.getNumeroSesiones())
                .build();
    }
}
