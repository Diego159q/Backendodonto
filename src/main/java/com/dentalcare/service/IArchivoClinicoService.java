package com.dentalcare.service;

import com.dentalcare.dto.request.ArchivoClinicoRequest;
import com.dentalcare.dto.response.ArchivoClinicoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IArchivoClinicoService {
    List<ArchivoClinicoResponse> listarPorPaciente(Long pacienteId);
    MensajeResponse subirArchivo(ArchivoClinicoRequest request, MultipartFile file);
}
