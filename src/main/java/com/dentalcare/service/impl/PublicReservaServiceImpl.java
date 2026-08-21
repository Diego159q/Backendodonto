package com.dentalcare.service.impl;

import com.dentalcare.dto.request.PublicReservaRequest;
import com.dentalcare.dto.response.PublicDisponibilidadResponse;
import com.dentalcare.dto.response.PublicReservaResponse;
import com.dentalcare.dto.response.PublicServicioResponse;
import com.dentalcare.entity.Cita;
import com.dentalcare.entity.EstadoCita;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.entity.Paciente;
import com.dentalcare.entity.Tratamiento;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.DuplicateResourceException;
import com.dentalcare.repository.CitaRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.repository.TratamientoRepository;
import com.dentalcare.repository.HorarioAtencionRepository;
import com.dentalcare.repository.BloqueoAgendaRepository;
import com.dentalcare.entity.HorarioAtencion;
import com.dentalcare.entity.BloqueoAgenda;
import com.dentalcare.service.IPublicReservaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicReservaServiceImpl implements IPublicReservaService {

    private final TratamientoRepository tratamientoRepository;
    private final CitaRepository citaRepository;
    private final OdontologoRepository odontologoRepository;
    private final PacienteRepository pacienteRepository;
    private final HorarioAtencionRepository horarioAtencionRepository;
    private final BloqueoAgendaRepository bloqueoAgendaRepository;

    public PublicReservaServiceImpl(TratamientoRepository tratamientoRepository,
                                    CitaRepository citaRepository,
                                    OdontologoRepository odontologoRepository,
                                    PacienteRepository pacienteRepository,
                                    HorarioAtencionRepository horarioAtencionRepository,
                                    BloqueoAgendaRepository bloqueoAgendaRepository) {
        this.tratamientoRepository = tratamientoRepository;
        this.citaRepository = citaRepository;
        this.odontologoRepository = odontologoRepository;
        this.pacienteRepository = pacienteRepository;
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.bloqueoAgendaRepository = bloqueoAgendaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicServicioResponse> listarServicios() {
        return tratamientoRepository.findByActivoTrue().stream()
                .map(t -> PublicServicioResponse.builder()
                        .id(t.getId())
                        .nombre(t.getNombre())
                        .descripcion(t.getDescripcion())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PublicDisponibilidadResponse obtenerDisponibilidad(LocalDate fecha, Long tratamientoId) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new BadRequestException("No se puede consultar disponibilidad en el pasado");
        }

        int duracionMinutos = 60; // Default
        if (tratamientoId != null) {
            Optional<Tratamiento> t = tratamientoRepository.findById(tratamientoId);
            if (t.isPresent() && t.get().getDuracionMinutos() != null) {
                duracionMinutos = t.get().getDuracionMinutos();
            }
        }

        List<BloqueoAgenda> bloqueosDelDia = bloqueoAgendaRepository.findByFecha(fecha);
        List<HorarioAtencion> horariosDelDia = horarioAtencionRepository.findByActivoTrueAndDiaSemana(fecha.getDayOfWeek());

        if (horariosDelDia.isEmpty()) {
            return new PublicDisponibilidadResponse(fecha.toString(), new ArrayList<>());
        }

        List<Odontologo> activos = odontologoRepository.findByActivoTrue();
        List<String> horariosDisponibles = new ArrayList<>();

        // Generate slots every 30 mins to offer
        LocalTime minApertura = horariosDelDia.stream().map(HorarioAtencion::getHoraInicio).min(LocalTime::compareTo).orElse(LocalTime.of(9, 0));
        LocalTime maxCierre = horariosDelDia.stream().map(HorarioAtencion::getHoraFin).max(LocalTime::compareTo).orElse(LocalTime.of(18, 0));

        LocalTime currentSlot = minApertura;
        while (currentSlot.isBefore(maxCierre) || currentSlot.equals(maxCierre)) {
            LocalTime slotEnd = currentSlot.plusMinutes(duracionMinutos);
            
            // Skip if slotEnd exceeds the absolute max clinic hours or spans next day (simplified check)
            if (slotEnd.isBefore(currentSlot) || slotEnd.isAfter(maxCierre)) {
                currentSlot = currentSlot.plusMinutes(30);
                continue;
            }

            if (fecha.isEqual(LocalDate.now()) && currentSlot.isBefore(LocalTime.now().plusHours(1))) {
                currentSlot = currentSlot.plusMinutes(30);
                continue;
            }

            final LocalTime fCurrentSlot = currentSlot;
            final LocalTime fSlotEnd = slotEnd;

            boolean algunaLibre = false;
            for (Odontologo o : activos) {
                // Check if this Odontologo works in this exact span
                boolean trabajaEnSlot = horariosDelDia.stream().anyMatch(h -> 
                    h.getOdontologo().getId().equals(o.getId()) &&
                    (h.getHoraInicio().isBefore(fCurrentSlot) || h.getHoraInicio().equals(fCurrentSlot)) &&
                    (h.getHoraFin().isAfter(fSlotEnd) || h.getHoraFin().equals(fSlotEnd))
                );

                if (!trabajaEnSlot) continue;

                // Check blockages
                boolean bloqueado = bloqueosDelDia.stream().anyMatch(b -> 
                    (b.getOdontologo() == null || b.getOdontologo().getId().equals(o.getId())) &&
                    (b.getHoraInicio() == null || isOverlapping(b.getHoraInicio(), b.getHoraFin(), fCurrentSlot, fSlotEnd))
                );

                if (bloqueado) continue;

                // Check existing appointments
                List<Cita> citasDelDoc = citaRepository.findByFechaAndOdontologoIdOrderByHoraInicio(fecha, o.getId());
                boolean ocupadoPorCita = citasDelDoc.stream().anyMatch(c -> 
                    c.getEstado() != EstadoCita.CANCELADA && 
                    isOverlapping(c.getHoraInicio(), c.getHoraFin(), fCurrentSlot, fSlotEnd)
                );

                if (!ocupadoPorCita) {
                    algunaLibre = true;
                    break; 
                }
            }

            if (algunaLibre) {
                horariosDisponibles.add(currentSlot.toString());
            }

            currentSlot = currentSlot.plusMinutes(30);
        }

        return new PublicDisponibilidadResponse(fecha.toString(), horariosDisponibles);
    }

    @Override
    public PublicReservaResponse agendarCita(PublicReservaRequest request) {
        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("No se puede agendar en el pasado");
        }
        
        Tratamiento tratamiento = tratamientoRepository.findById(request.getTratamientoId())
                .orElseThrow(() -> new BadRequestException("Servicio inválido"));

        int duracion = tratamiento.getDuracionMinutos() != null ? tratamiento.getDuracionMinutos() : 60;
        LocalTime horaFin = request.getHoraInicio().plusMinutes(duracion);

        List<BloqueoAgenda> bloqueosDelDia = bloqueoAgendaRepository.findByFecha(request.getFecha());
        List<HorarioAtencion> horariosDelDia = horarioAtencionRepository.findByActivoTrueAndDiaSemana(request.getFecha().getDayOfWeek());

        List<Odontologo> activos = odontologoRepository.findByActivoTrue();
        Odontologo doctorAsignado = null;

        for (Odontologo o : activos) {
            boolean trabajaEnSlot = horariosDelDia.stream().anyMatch(h -> 
                h.getOdontologo().getId().equals(o.getId()) &&
                (h.getHoraInicio().isBefore(request.getHoraInicio()) || h.getHoraInicio().equals(request.getHoraInicio())) &&
                (h.getHoraFin().isAfter(horaFin) || h.getHoraFin().equals(horaFin))
            );
            if (!trabajaEnSlot) continue;

            boolean bloqueado = bloqueosDelDia.stream().anyMatch(b -> 
                (b.getOdontologo() == null || b.getOdontologo().getId().equals(o.getId())) &&
                (b.getHoraInicio() == null || isOverlapping(b.getHoraInicio(), b.getHoraFin(), request.getHoraInicio(), horaFin))
            );
            if (bloqueado) continue;

            List<Cita> citasDelDoc = citaRepository.findByFechaAndOdontologoIdOrderByHoraInicio(request.getFecha(), o.getId());
            boolean ocupado = citasDelDoc.stream().anyMatch(c -> 
                c.getEstado() != EstadoCita.CANCELADA && 
                isOverlapping(c.getHoraInicio(), c.getHoraFin(), request.getHoraInicio(), horaFin)
            );

            if (!ocupado) {
                doctorAsignado = o;
                break;
            }
        }

        if (doctorAsignado == null) {
            throw new DuplicateResourceException("El horario seleccionado ya no está disponible o se solapa con otra cita.");
        }

        Optional<Paciente> pacienteOpt = pacienteRepository.findByDni(request.getDni());
        Paciente paciente;
        if (pacienteOpt.isPresent()) {
            paciente = pacienteOpt.get();
        } else {
            paciente = new Paciente();
            paciente.setDni(request.getDni());
            paciente.setNombres(request.getNombres());
            paciente.setApellidos(request.getApellidos());
            paciente.setTelefono(request.getTelefono());
            paciente.setEmail(request.getEmail());
            paciente.setActivo(true);
            
            Optional<Paciente> ultimo = pacienteRepository.findTopByOrderByIdDesc();
            String prefix = "PAC-";
            String codigo = prefix + "0001";
            if (ultimo.isPresent() && ultimo.get().getCodigoPaciente() != null && ultimo.get().getCodigoPaciente().startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(ultimo.get().getCodigoPaciente().substring(4));
                    codigo = prefix + String.format("%04d", num + 1);
                } catch (Exception ignored) {}
            }
            paciente.setCodigoPaciente(codigo);
            paciente = pacienteRepository.save(paciente);
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setOdontologo(doctorAsignado);
        cita.setFecha(request.getFecha());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(horaFin);
        cita.setMotivo(tratamiento.getNombre() + " (Reserva Pública)");
        cita.setTipoAtencion("PÚBLICA");
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setObservaciones("Creada desde la web pública");

        citaRepository.save(cita);

        return PublicReservaResponse.builder()
                .success(true)
                .message("Su solicitud de cita ha sido recibida correctamente.")
                .reference(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();
    }

    private boolean isOverlapping(LocalTime existingStart, LocalTime existingEnd,
                                  LocalTime newStart, LocalTime newEnd) {
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }
}
