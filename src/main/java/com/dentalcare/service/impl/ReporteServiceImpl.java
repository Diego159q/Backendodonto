package com.dentalcare.service.impl;

import com.dentalcare.dto.response.ReporteResponse;
import com.dentalcare.entity.*;
import com.dentalcare.entity.EstadoPago;
import com.dentalcare.repository.CitaRepository;
import com.dentalcare.repository.PacienteRepository;
import com.dentalcare.repository.PagoRepository;
import com.dentalcare.service.IReporteService;
import com.dentalcare.util.ExcelGenerator;
import com.dentalcare.util.PdfGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReporteServiceImpl implements IReporteService {

    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;
    private final PacienteRepository pacienteRepository;
    private final PdfGenerator pdfGenerator;
    private final ExcelGenerator excelGenerator;

    public ReporteServiceImpl(CitaRepository citaRepository,
                              PagoRepository pagoRepository,
                              PacienteRepository pacienteRepository,
                              PdfGenerator pdfGenerator,
                              ExcelGenerator excelGenerator) {
        this.citaRepository = citaRepository;
        this.pagoRepository = pagoRepository;
        this.pacienteRepository = pacienteRepository;
        this.pdfGenerator = pdfGenerator;
        this.excelGenerator = excelGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteResponse generarReporte(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Map<String, Object>> datos = generarDatos(tipo, fechaInicio, fechaFin);

        return ReporteResponse.builder()
                .tipo(tipo)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .datos(datos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportarPdf(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Map<String, Object>> datos = generarDatos(tipo, fechaInicio, fechaFin);

        String titulo = "Reporte de " + tipo;
        String[] headers = datos.isEmpty() ? new String[0] : datos.get(0).keySet().toArray(new String[0]);
        String[][] data = new String[datos.size()][headers.length];

        for (int i = 0; i < datos.size(); i++) {
            Map<String, Object> row = datos.get(i);
            for (int j = 0; j < headers.length; j++) {
                Object val = row.get(headers[j]);
                data[i][j] = val != null ? val.toString() : "";
            }
        }

        return pdfGenerator.generateReportePdf(titulo, data, headers);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportarExcel(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Map<String, Object>> datos = generarDatos(tipo, fechaInicio, fechaFin);

        String[] headers = datos.isEmpty() ? new String[0] : datos.get(0).keySet().toArray(new String[0]);
        List<String[]> data = new ArrayList<>();
        for (Map<String, Object> row : datos) {
            String[] rowData = new String[headers.length];
            for (int j = 0; j < headers.length; j++) {
                Object val = row.get(headers[j]);
                rowData[j] = val != null ? val.toString() : "";
            }
            data.add(rowData);
        }

        return excelGenerator.generateExcelReport("Reporte " + tipo, headers, data);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportarCsv(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Map<String, Object>> datos = generarDatos(tipo, fechaInicio, fechaFin);

        StringBuilder csv = new StringBuilder();
        if (!datos.isEmpty()) {
            String[] headers = datos.get(0).keySet().toArray(new String[0]);
            csv.append(String.join(",", headers)).append("\n");

            for (Map<String, Object> row : datos) {
                List<String> values = new ArrayList<>();
                for (String h : headers) {
                    Object val = row.get(h);
                    values.add(val != null ? val.toString() : "");
                }
                csv.append(String.join(",", values)).append("\n");
            }
        }

        return csv.toString().getBytes();
    }

    private List<Map<String, Object>> generarDatos(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        switch (tipo.toUpperCase()) {
            case "CITAS":
                return generarReporteCitas(fechaInicio, fechaFin);
            case "PAGOS":
                return generarReportePagos(fechaInicio, fechaFin);
            case "PACIENTES":
                return generarReportePacientes(fechaInicio, fechaFin);
            case "INGRESOS":
                return generarReporteIngresos(fechaInicio, fechaFin);
            default:
                return new ArrayList<>();
        }
    }

    private List<Map<String, Object>> generarReporteCitas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Cita> citas;
        if (fechaInicio != null && fechaFin != null) {
            citas = citaRepository.findByFechaBetween(fechaInicio, fechaFin);
        } else {
            citas = citaRepository.findAll();
        }

        return citas.stream().map(c -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Paciente", c.getPaciente() != null ?
                    c.getPaciente().getNombres() + " " + c.getPaciente().getApellidos() : "");
            map.put("Odont\u00f3logo", c.getOdontologo() != null ?
                    c.getOdontologo().getNombres() + " " + c.getOdontologo().getApellidos() : "");
            map.put("Fecha", c.getFecha() != null ? c.getFecha().toString() : "");
            map.put("Hora Inicio", c.getHoraInicio() != null ? c.getHoraInicio().toString() : "");
            map.put("Hora Fin", c.getHoraFin() != null ? c.getHoraFin().toString() : "");
            map.put("Estado", c.getEstado() != null ? c.getEstado().name() : "");
            map.put("Motivo", c.getMotivo() != null ? c.getMotivo() : "");
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> generarReportePagos(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pago> pagos;
        if (fechaInicio != null && fechaFin != null) {
            pagos = pagoRepository.findByFechaBetween(fechaInicio, fechaFin);
        } else {
            pagos = pagoRepository.findAll();
        }

        return pagos.stream().map(p -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("N\u00famero", p.getNumeroPago() != null ? p.getNumeroPago() : "");
            map.put("Paciente", p.getPaciente() != null ?
                    p.getPaciente().getNombres() + " " + p.getPaciente().getApellidos() : "");
            map.put("Monto", p.getMonto() != null ? p.getMonto().toString() : "0");
            map.put("M\u00e9todo", p.getMetodoPago() != null ? p.getMetodoPago().name() : "");
            map.put("Fecha", p.getFecha() != null ? p.getFecha().toString() : "");
            map.put("Estado", p.getEstado() != null ? p.getEstado().name() : "");
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> generarReportePacientes(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Paciente> pacientes;
        if (fechaInicio != null && fechaFin != null) {
            pacientes = pacienteRepository.findAll().stream()
                    .filter(p -> p.getFechaCreacion() != null &&
                            p.getFechaCreacion().toLocalDate().compareTo(fechaInicio) >= 0 &&
                            p.getFechaCreacion().toLocalDate().compareTo(fechaFin) <= 0)
                    .collect(Collectors.toList());
        } else {
            pacientes = pacienteRepository.findAll();
        }

        return pacientes.stream().map(p -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("C\u00f3digo", p.getCodigoPaciente() != null ? p.getCodigoPaciente() : "");
            map.put("Nombres", p.getNombres() != null ? p.getNombres() : "");
            map.put("Apellidos", p.getApellidos() != null ? p.getApellidos() : "");
            map.put("DNI", p.getDni() != null ? p.getDni() : "");
            map.put("Tel\u00e9fono", p.getTelefono() != null ? p.getTelefono() : "");
            map.put("Email", p.getEmail() != null ? p.getEmail() : "");
            map.put("Estado", p.getActivo() != null && p.getActivo() ? "Activo" : "Inactivo");
            return map;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> generarReporteIngresos(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null) fechaInicio = LocalDate.now().withDayOfMonth(1);
        if (fechaFin == null) fechaFin = LocalDate.now();

        List<Pago> pagos = pagoRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .filter(p -> p.getEstado() == EstadoPago.PENDIENTE || p.getEstado() == EstadoPago.PAGADO)
                .collect(Collectors.toList());

        Map<LocalDate, BigDecimal> ingresosPorDia = pagos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFecha() != null ? p.getFecha() : LocalDate.now(),
                        Collectors.reducing(BigDecimal.ZERO, p -> p.getMonto() != null ? p.getMonto() : BigDecimal.ZERO, BigDecimal::add)
                ));

        List<Map<String, Object>> datos = new ArrayList<>();
        ingresosPorDia.forEach((fecha, total) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Fecha", fecha.toString());
            map.put("Total", total);
            datos.add(map);
        });

        datos.sort(Comparator.comparing(m -> m.get("Fecha").toString()));
        return datos;
    }
}
