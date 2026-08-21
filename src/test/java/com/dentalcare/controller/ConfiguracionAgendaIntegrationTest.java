package com.dentalcare.controller;

import com.dentalcare.dto.request.BloqueoAgendaRequest;
import com.dentalcare.dto.request.HorarioAtencionRequest;
import com.dentalcare.dto.response.BloqueoAgendaResponse;
import com.dentalcare.dto.response.HorarioAtencionResponse;
import com.dentalcare.entity.Odontologo;
import com.dentalcare.repository.BloqueoAgendaRepository;
import com.dentalcare.repository.HorarioAtencionRepository;
import com.dentalcare.repository.OdontologoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConfiguracionAgendaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HorarioAtencionRepository horarioAtencionRepository;

    @Autowired
    private BloqueoAgendaRepository bloqueoAgendaRepository;

    @Autowired
    private OdontologoRepository odontologoRepository;

    private Odontologo odontologoTest;

    @BeforeEach
    void setUp() {
        bloqueoAgendaRepository.deleteAll();
        horarioAtencionRepository.deleteAll();
        odontologoRepository.deleteAll();

        odontologoTest = new Odontologo();
        odontologoTest.setNombres("Juan");
        odontologoTest.setApellidos("Pérez");
        odontologoTest.setEspecialidad("General");
        odontologoTest.setTelefono("123456789");
        odontologoTest.setEmail("juan.perez@test.com");
        odontologoTest = odontologoRepository.save(odontologoTest);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testCrearHorario_Success() throws Exception {
        HorarioAtencionRequest request = new HorarioAtencionRequest();
        request.setOdontologoId(odontologoTest.getId());
        request.setDiaSemana(DayOfWeek.MONDAY);
        request.setHoraInicio(LocalTime.of(9, 0));
        request.setHoraFin(LocalTime.of(13, 0));
        request.setActivo(true);

        mockMvc.perform(post("/horarios-atencion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertThat(horarioAtencionRepository.findAll()).hasSize(1);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testCrearHorario_InvalidHora_Returns400() throws Exception {
        HorarioAtencionRequest request = new HorarioAtencionRequest();
        request.setOdontologoId(odontologoTest.getId());
        request.setDiaSemana(DayOfWeek.MONDAY);
        request.setHoraInicio(LocalTime.of(13, 0));
        request.setHoraFin(LocalTime.of(9, 0)); // Invalido: fin < inicio
        request.setActivo(true);

        mockMvc.perform(post("/horarios-atencion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testCrearHorario_Superpuesto_Returns409() throws Exception {
        HorarioAtencionRequest request1 = new HorarioAtencionRequest();
        request1.setOdontologoId(odontologoTest.getId());
        request1.setDiaSemana(DayOfWeek.MONDAY);
        request1.setHoraInicio(LocalTime.of(9, 0));
        request1.setHoraFin(LocalTime.of(13, 0));
        request1.setActivo(true);

        mockMvc.perform(post("/horarios-atencion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        HorarioAtencionRequest request2 = new HorarioAtencionRequest();
        request2.setOdontologoId(odontologoTest.getId());
        request2.setDiaSemana(DayOfWeek.MONDAY);
        request2.setHoraInicio(LocalTime.of(12, 0)); // Superpone con el bloque 9-13
        request2.setHoraFin(LocalTime.of(15, 0));
        request2.setActivo(true);

        mockMvc.perform(post("/horarios-atencion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testCrearBloqueo_Global() throws Exception {
        BloqueoAgendaRequest request = new BloqueoAgendaRequest();
        request.setOdontologoId(null); // Global
        request.setFecha(LocalDate.now().plusDays(1));
        request.setMotivo("Feriado");

        mockMvc.perform(post("/bloqueos-agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertThat(bloqueoAgendaRepository.findAll()).hasSize(1);
        assertThat(bloqueoAgendaRepository.findAll().get(0).getOdontologo()).isNull();
    }

    @Test
    void testAccesos_NoAutenticado_Returns401() throws Exception {
        mockMvc.perform(get("/horarios-atencion"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void testAccesos_RolInvalido_Returns403() throws Exception {
        mockMvc.perform(get("/horarios-atencion"))
                .andExpect(status().isForbidden());
    }
}
