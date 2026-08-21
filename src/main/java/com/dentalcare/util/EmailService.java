package com.dentalcare.util;

import com.dentalcare.entity.Cita;
import com.dentalcare.entity.Pago;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendCitaRecordatorio(Cita cita) {
        String to = cita.getPaciente().getEmail();
        String subject = "Recordatorio de Cita - DentalCare";
        String body = String.format(
                "Estimado(a) %s %s,\n\n"
                + "Le recordamos que tiene una cita programada:\n"
                + "Fecha: %s\n"
                + "Hora: %s\n"
                + "Odontólogo: %s %s\n"
                + "Motivo: %s\n\n"
                + "Por favor, asista puntualmente.\n\n"
                + "Saludos,\nDentalCare",
                cita.getPaciente().getNombres(),
                cita.getPaciente().getApellidos(),
                cita.getFecha().toString(),
                cita.getHoraInicio(),
                cita.getOdontologo().getUsuario().getNombres(),
                cita.getOdontologo().getUsuario().getApellidos(),
                cita.getMotivo()
        );
        sendEmail(to, subject, body);
    }

    public void sendPagoRecordatorio(Pago pago) {
        String to = pago.getPaciente().getEmail();
        String subject = "Recordatorio de Pago - DentalCare";
        String body = String.format(
                "Estimado(a) %s %s,\n\n"
                + "Le recordamos que tiene un pago pendiente:\n"
                + "Monto: S/ %.2f\n"
                + "Fecha: %s\n"
                + "Referencia: %s\n\n"
                + "Por favor, realice el pago a la brevedad.\n\n"
                + "Saludos,\nDentalCare",
                pago.getPaciente().getNombres(),
                pago.getPaciente().getApellidos(),
                pago.getMonto(),
                pago.getFecha().toString(),
                pago.getNumeroOperacion()
        );
        sendEmail(to, subject, body);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email HTML", e);
        }
    }
}
