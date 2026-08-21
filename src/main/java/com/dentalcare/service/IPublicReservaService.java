package com.dentalcare.service;

import com.dentalcare.dto.request.PublicReservaRequest;
import com.dentalcare.dto.response.PublicDisponibilidadResponse;
import com.dentalcare.dto.response.PublicReservaResponse;
import com.dentalcare.dto.response.PublicServicioResponse;

import java.time.LocalDate;
import java.util.List;

public interface IPublicReservaService {
    List<PublicServicioResponse> listarServicios();
    PublicDisponibilidadResponse obtenerDisponibilidad(LocalDate fecha, Long tratamientoId);
    PublicReservaResponse agendarCita(PublicReservaRequest request);
}
