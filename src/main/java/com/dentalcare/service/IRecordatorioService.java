package com.dentalcare.service;

import com.dentalcare.dto.request.RecordatorioRequest;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.dto.response.RecordatorioResponse;

import java.util.List;

public interface IRecordatorioService {
    List<RecordatorioResponse> listarPorPaciente(Long pacienteId);
    MensajeResponse crear(RecordatorioRequest request);
    MensajeResponse enviarPendientes();
}
