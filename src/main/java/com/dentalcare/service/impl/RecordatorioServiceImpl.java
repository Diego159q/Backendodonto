package com.dentalcare.service.impl;

import com.dentalcare.dto.request.RecordatorioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.RecordatorioResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.CitaRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.repository.RecordatorioRepository;
import com.dentalcare.service.IRecordatorioService;
import com.dentalcare.util.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordatorioServiceImpl implements IRecordatorioService {

    private final RecordatorioRepository recordatorioRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final EmailService emailService;

    public RecordatorioServiceImpl(RecordatorioRepository recordatorioRepository,
                                   PacienteRepository pacienteRepository,
                                   CitaRepository citaRepository,
                                   EmailService emailService) {
        this.recordatorioRepository = recordatorioRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordatorioResponse> listarPorPaciente(Long pacienteId) {
        return recordatorioRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::toRecordatorioResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse crear(RecordatorioRequest request) {
        Paciente paciente = null;
        if (request.getPacienteId() != null) {
            paciente = pacienteRepository.findById(request.getPacienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));
        }

        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setPaciente(paciente);
        recordatorio.setTipo(TipoRecordatorio.valueOf(request.getTipo().toUpperCase()));
        recordatorio.setMensaje(request.getMensaje());
        recordatorio.setFechaProgramada(request.getFechaProgramada() != null ?
                request.getFechaProgramada() : LocalDateTime.now());
        recordatorio.setEnviado(false);
        recordatorio.setMedio(request.getMedio() != null ?
                MedioRecordatorio.valueOf(request.getMedio().toUpperCase()) : MedioRecordatorio.CORREO);

        if (request.getCitaId() != null) {
            Cita cita = citaRepository.findById(request.getCitaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", request.getCitaId()));
            recordatorio.setCita(cita);
        }

        recordatorioRepository.save(recordatorio);

        return MensajeResponse.builder()
                .mensaje("Recordatorio programado exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public MensajeResponse enviarPendientes() {
        LocalDateTime now = LocalDateTime.now();
        List<Recordatorio> pendientes = recordatorioRepository
                .findByEnviadoFalseAndFechaProgramadaBefore(now);

        int enviados = 0;
        for (Recordatorio recordatorio : pendientes) {
            try {
                if (recordatorio.getPaciente() != null &&
                        recordatorio.getPaciente().getEmail() != null) {
                    emailService.sendEmail(
                            recordatorio.getPaciente().getEmail(),
                            "Recordatorio - DentalCare",
                            recordatorio.getMensaje()
                    );
                }
                recordatorio.setEnviado(true);
                recordatorio.setFechaEnvio(LocalDateTime.now());
                recordatorioRepository.save(recordatorio);
                enviados++;
            } catch (Exception e) {
                recordatorio.setEnviado(false);
                recordatorioRepository.save(recordatorio);
            }
        }

        return MensajeResponse.builder()
                .mensaje(enviados + " recordatorios enviados exitosamente")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private RecordatorioResponse toRecordatorioResponse(Recordatorio recordatorio) {
        return RecordatorioResponse.builder()
                .id(recordatorio.getId())
                .pacienteId(recordatorio.getPaciente() != null ? recordatorio.getPaciente().getId() : null)
                .pacienteNombre(recordatorio.getPaciente() != null ?
                        recordatorio.getPaciente().getNombres() + " " + recordatorio.getPaciente().getApellidos() : null)
                .citaId(recordatorio.getCita() != null ? recordatorio.getCita().getId() : null)
                .tipo(recordatorio.getTipo() != null ? recordatorio.getTipo().name() : null)
                .mensaje(recordatorio.getMensaje())
                .fechaProgramada(recordatorio.getFechaProgramada())
                .enviado(recordatorio.getEnviado())
                .medio(recordatorio.getMedio() != null ? recordatorio.getMedio().name() : null)
                .fechaEnvio(recordatorio.getFechaEnvio())
                .fechaCreacion(recordatorio.getFechaCreacion())
                .build();
    }
}
