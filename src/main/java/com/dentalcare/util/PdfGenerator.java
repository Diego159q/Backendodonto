package com.dentalcare.util;

import com.dentalcare.entity.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfGenerator {

    public byte[] generateRecetaPdf(Receta receta, List<RecetaDetalle> detalles) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("RECETA MÉDICA").setBold().setFontSize(18));
        document.add(new Paragraph("Paciente: " + receta.getPaciente().getNombres() + " "
                + receta.getPaciente().getApellidos()));
        document.add(new Paragraph("Odontólogo: " + receta.getOdontologo().getUsuario().getNombres() + " "
                + receta.getOdontologo().getUsuario().getApellidos()));
        document.add(new Paragraph("Fecha: " + receta.getFecha().toString()));
        document.add(new Paragraph("\nIndicaciones: " + receta.getObservaciones()));
        document.add(new Paragraph("\nMedicamentos:"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 20, 20, 20}));
        table.addHeaderCell(new Cell().add(new Paragraph("Medicamento").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Dosis").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Frecuencia").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Duración").setBold()));

        for (RecetaDetalle detalle : detalles) {
            table.addCell(new Cell().add(new Paragraph(detalle.getMedicamento().getNombre())));
            table.addCell(new Cell().add(new Paragraph(detalle.getDosis())));
            table.addCell(new Cell().add(new Paragraph(detalle.getFrecuencia())));
            table.addCell(new Cell().add(new Paragraph(detalle.getDuracion())));
        }

        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    public byte[] generateOdontogramaPdf(Odontograma odontograma) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("ODONTOGRAMA").setBold().setFontSize(18));
        document.add(new Paragraph("Paciente: " + odontograma.getPaciente().getNombres() + " "
                + odontograma.getPaciente().getApellidos()));
        document.add(new Paragraph("Odontólogo: " + odontograma.getOdontologo().getUsuario().getNombres() + " "
                + odontograma.getOdontologo().getUsuario().getApellidos()));
        document.add(new Paragraph("Fecha: " + odontograma.getFecha().toString()));
        document.add(new Paragraph("Tipo: " + odontograma.getTipoDenticion()));
        document.add(new Paragraph("Observaciones: " + odontograma.getObservaciones()));

        document.close();
        return baos.toByteArray();
    }

    public byte[] generatePlanTratamientoPdf(PlanTratamiento plan, List<PlanTratamientoDetalle> detalles) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("PLAN DE TRATAMIENTO").setBold().setFontSize(18));
        document.add(new Paragraph("Paciente: " + plan.getPaciente().getNombres() + " "
                + plan.getPaciente().getApellidos()));
        document.add(new Paragraph("Odontólogo: " + plan.getOdontologo().getUsuario().getNombres() + " "
                + plan.getOdontologo().getUsuario().getApellidos()));
        document.add(new Paragraph("Fecha: " + plan.getFecha().toString()));
        document.add(new Paragraph("Título: " + "Plan de tratamiento"));
        document.add(new Paragraph("Descripción: " + plan.getObservaciones()));
        document.add(new Paragraph("\nDetalles del tratamiento:"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 25, 25}));
        table.addHeaderCell(new Cell().add(new Paragraph("Tratamiento").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Costo").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Observaciones").setBold()));

        for (PlanTratamientoDetalle detalle : detalles) {
            table.addCell(new Cell().add(new Paragraph(
                    detalle.getTratamiento() != null && detalle.getTratamiento().getTratamiento() != null ? detalle.getTratamiento().getTratamiento().getNombre() : "")));
            table.addCell(new Cell().add(new Paragraph(
                    detalle.getSubtotal() != null ? detalle.getSubtotal().toString() : "")));
            table.addCell(new Cell().add(new Paragraph(detalle.getEstado())));
        }

        document.add(table);
        document.add(new Paragraph("\nCosto Total: S/ " + plan.getMontoFinal()));
        document.close();
        return baos.toByteArray();
    }

    public byte[] generateComprobantePagoPdf(Pago pago) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("COMPROBANTE DE PAGO").setBold().setFontSize(18));
        document.add(new Paragraph("Paciente: " + pago.getPaciente().getNombres() + " "
                + pago.getPaciente().getApellidos()));
        document.add(new Paragraph("Monto: S/ " + pago.getMonto()));
        document.add(new Paragraph("Método de Pago: " + pago.getMetodoPago()));
        document.add(new Paragraph("Referencia: " + pago.getNumeroOperacion()));
        document.add(new Paragraph("Fecha: " + pago.getFecha().toString()));
        document.add(new Paragraph("Estado: " + pago.getEstado()));
        document.add(new Paragraph("Observaciones: " + pago.getObservaciones()));

        document.close();
        return baos.toByteArray();
    }

    public byte[] generateReportePdf(String titulo, String[][] data, String[] headers) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph(titulo).setBold().setFontSize(18));

        Table table = new Table(UnitValue.createPercentArray(
                new float[headers.length])).useAllAvailableWidth();

        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setBold()));
        }

        for (String[] row : data) {
            for (String cell : row) {
                table.addCell(new Cell().add(new Paragraph(cell)));
            }
        }

        document.add(table);
        document.close();
        return baos.toByteArray();
    }
}
