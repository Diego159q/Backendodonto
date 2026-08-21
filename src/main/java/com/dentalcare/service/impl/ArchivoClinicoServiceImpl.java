package com.dentalcare.service.impl;

import com.dentalcare.dto.request.ArchivoClinicoRequest;
import com.dentalcare.dto.response.ArchivoClinicoResponse;
import com.dentalcare.dto.response.MensajeResponse;
import com.dentalcare.entity.*;
import com.dentalcare.exception.BadRequestException;
import com.dentalcare.exception.ResourceNotFoundException;
import com.dentalcare.repository.ArchivoClinicoRepository;
import com.dentalcare.repository.HistoriaClinicaRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.repository.UsuarioRepository;
import com.dentalcare.service.IArchivoClinicoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArchivoClinicoServiceImpl implements IArchivoClinicoService {

    private final ArchivoClinicoRepository archivoClinicoRepository;
    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final UsuarioRepository usuarioRepository;

    private final String uploadDir = "uploads/archivos/";

    public ArchivoClinicoServiceImpl(ArchivoClinicoRepository archivoClinicoRepository,
                                     PacienteRepository pacienteRepository,
                                     HistoriaClinicaRepository historiaClinicaRepository,
                                     UsuarioRepository usuarioRepository) {
        this.archivoClinicoRepository = archivoClinicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArchivoClinicoResponse> listarPorPaciente(Long pacienteId) {
        return archivoClinicoRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::toArchivoClinicoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponse subirArchivo(ArchivoClinicoRequest request, MultipartFile file) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", request.getPacienteId()));

        if (file.isEmpty()) {
            throw new BadRequestException("El archivo no puede estar vac\u00edo");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new BadRequestException("Nombre de archivo inv\u00e1lido");
        }

        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) extension = originalFilename.substring(i).toLowerCase();

        List<String> allowedExtensions = List.of(".pdf", ".jpg", ".jpeg", ".png", ".doc", ".docx", ".xls", ".xlsx");
        if (!allowedExtensions.contains(extension)) {
            throw new BadRequestException("Tipo de archivo no permitido. Extensiones permitidas: " + allowedExtensions);
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("El archivo no puede superar los 10 MB");
        }

        try {
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path uploadPath = Paths.get(uploadDir + paciente.getId() + "/");
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            ArchivoClinico archivo = new ArchivoClinico();
            archivo.setPaciente(paciente);
            archivo.setTipoArchivo(TipoArchivo.valueOf(request.getTipoArchivo().toUpperCase()));
            archivo.setNombreArchivo(originalFilename);
            archivo.setUrl(filePath.toString());
            archivo.setTamano(file.getSize());
            archivo.setDescripcion(request.getDescripcion());

            if (request.getHistoriaClinicaId() != null) {
                HistoriaClinica hc = historiaClinicaRepository.findById(request.getHistoriaClinicaId())
                        .orElseThrow(() -> new ResourceNotFoundException("HistoriaClinica", "id",
                                request.getHistoriaClinicaId()));
                archivo.setHistoriaClinica(hc);
            }

            archivoClinicoRepository.save(archivo);

            return MensajeResponse.builder()
                    .mensaje("Archivo subido exitosamente")
                    .success(true)
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (IOException e) {
            throw new BadRequestException("Error al subir el archivo: " + e.getMessage());
        }
    }

    private ArchivoClinicoResponse toArchivoClinicoResponse(ArchivoClinico archivo) {
        return ArchivoClinicoResponse.builder()
                .id(archivo.getId())
                .pacienteId(archivo.getPaciente() != null ? archivo.getPaciente().getId() : null)
                .pacienteNombre(archivo.getPaciente() != null ?
                        archivo.getPaciente().getNombres() + " " + archivo.getPaciente().getApellidos() : null)
                .historiaClinicaId(archivo.getHistoriaClinica() != null ? archivo.getHistoriaClinica().getId() : null)
                .tipoArchivo(archivo.getTipoArchivo() != null ? archivo.getTipoArchivo().name() : null)
                .nombreArchivo(archivo.getNombreArchivo())
                .url(archivo.getUrl())
                .tamano(archivo.getTamano())
                .descripcion(archivo.getDescripcion())
                .fechaSubida(archivo.getFechaSubida())
                .build();
    }
}
