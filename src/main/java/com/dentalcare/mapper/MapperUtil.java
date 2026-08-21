package com.dentalcare.mapper;

import com.dentalcare.dto.response.CitaResponse;
import com.dentalcare.dto.response.OdontogramaDetalleResponse;
import com.dentalcare.dto.response.OdontogramaResponse;
import com.dentalcare.dto.response.PacienteResponse;
import com.dentalcare.dto.response.PagoResponse;
import com.dentalcare.dto.response.ProductoResponse;
import com.dentalcare.dto.response.RecetaDetalleResponse;
import com.dentalcare.dto.response.RecetaResponse;
import com.dentalcare.dto.response.UsuarioResponse;
import com.dentalcare.entity.Cita;
import com.dentalcare.entity.Odontograma;
import com.dentalcare.entity.OdontogramaDetalle;
import com.dentalcare.entity.Paciente;
import com.dentalcare.entity.Pago;
import com.dentalcare.entity.Producto;
import com.dentalcare.entity.Receta;
import com.dentalcare.entity.RecetaDetalle;
import com.dentalcare.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil {

    public UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .username(usuario.getUsername())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .rolId(usuario.getRol() != null ? usuario.getRol().getId() : null)
                .activo(usuario.getActivo())
                .bloqueado(usuario.getBloqueado())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }

    public List<UsuarioResponse> toUsuarioResponseList(List<Usuario> usuarios) {
        if (usuarios == null) return Collections.emptyList();
        return usuarios.stream().map(this::toUsuarioResponse).collect(Collectors.toList());
    }

    public PacienteResponse toPacienteResponse(Paciente paciente) {
        if (paciente == null) return null;
        return PacienteResponse.builder()
                .id(paciente.getId())
                .codigoPaciente(paciente.getCodigoPaciente())
                .nombres(paciente.getNombres())
                .apellidos(paciente.getApellidos())
                .dni(paciente.getDni())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .edad(calcularEdad(paciente.getFechaNacimiento()))
                .sexo(paciente.getSexo())
                .telefono(paciente.getTelefono())
                .email(paciente.getEmail())
                .direccion(paciente.getDireccion())
                .distrito(paciente.getDistrito())
                .ciudad(paciente.getCiudad())
                .estadoCivil(paciente.getEstadoCivil())
                .ocupacion(paciente.getOcupacion())
                .tipoSangre(paciente.getTipoSangre())
                .alergias(paciente.getAlergias())
                .enfermedadesPrevias(paciente.getEnfermedadesPrevias())
                .medicamentosActuales(paciente.getMedicamentosActuales())
                .contactoEmergencia(paciente.getContactoEmergencia())
                .telefonoEmergencia(paciente.getTelefonoEmergencia())
                .observaciones(paciente.getObservaciones())
                .activo(paciente.getActivo())
                .fechaRegistro(paciente.getFechaRegistro())
                .fechaCreacion(paciente.getFechaCreacion())
                .build();
    }

    public List<PacienteResponse> toPacienteResponseList(List<Paciente> pacientes) {
        if (pacientes == null) return Collections.emptyList();
        return pacientes.stream().map(this::toPacienteResponse).collect(Collectors.toList());
    }

    private Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return null;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public CitaResponse toCitaResponse(Cita cita) {
        if (cita == null) return null;
        return CitaResponse.builder()
                .id(cita.getId())
                .pacienteId(cita.getPaciente() != null ? cita.getPaciente().getId() : null)
                .pacienteNombre(combinarNombreApellido(
                        cita.getPaciente() != null ? cita.getPaciente().getNombres() : null,
                        cita.getPaciente() != null ? cita.getPaciente().getApellidos() : null))
                .odontologoId(cita.getOdontologo() != null ? cita.getOdontologo().getId() : null)
                .odontologoNombre(combinarNombreApellido(
                        cita.getOdontologo() != null ? cita.getOdontologo().getNombres() : null,
                        cita.getOdontologo() != null ? cita.getOdontologo().getApellidos() : null))
                .fecha(cita.getFecha())
                .horaInicio(cita.getHoraInicio())
                .horaFin(cita.getHoraFin())
                .motivo(cita.getMotivo())
                .tipoAtencion(cita.getTipoAtencion())
                .consultorio(cita.getConsultorio())
                .estado(cita.getEstado() != null ? cita.getEstado().name() : null)
                .observaciones(cita.getObservaciones())
                .motivoCancelacion(cita.getMotivoCancelacion())
                .fechaCreacion(cita.getFechaCreacion())
                .build();
    }

    public List<CitaResponse> toCitaResponseList(List<Cita> citas) {
        if (citas == null) return Collections.emptyList();
        return citas.stream().map(this::toCitaResponse).collect(Collectors.toList());
    }

    public OdontogramaResponse toOdontogramaResponse(Odontograma odontograma) {
        if (odontograma == null) return null;
        return OdontogramaResponse.builder()
                .id(odontograma.getId())
                .pacienteId(odontograma.getPaciente() != null ? odontograma.getPaciente().getId() : null)
                .pacienteNombre(combinarNombreApellido(
                        odontograma.getPaciente() != null ? odontograma.getPaciente().getNombres() : null,
                        odontograma.getPaciente() != null ? odontograma.getPaciente().getApellidos() : null))
                .odontologoId(odontograma.getOdontologo() != null ? odontograma.getOdontologo().getId() : null)
                .odontologoNombre(combinarNombreApellido(
                        odontograma.getOdontologo() != null ? odontograma.getOdontologo().getNombres() : null,
                        odontograma.getOdontologo() != null ? odontograma.getOdontologo().getApellidos() : null))
                .fecha(odontograma.getFecha())
                .tipoDenticion(odontograma.getTipoDenticion())
                .observaciones(odontograma.getObservaciones())
                .estado(odontograma.getEstado())
                .activo(odontograma.getActivo())
                .detalles(java.util.Collections.emptyList())
                .fechaCreacion(odontograma.getFechaCreacion())
                .build();
    }

    public List<OdontogramaResponse> toOdontogramaResponseList(List<Odontograma> odontogramas) {
        if (odontogramas == null) return Collections.emptyList();
        return odontogramas.stream().map(this::toOdontogramaResponse).collect(Collectors.toList());
    }

    public OdontogramaDetalleResponse toOdontogramaDetalleResponse(OdontogramaDetalle detalle) {
        if (detalle == null) return null;
        return OdontogramaDetalleResponse.builder()
                .id(detalle.getId())
                .odontogramaId(detalle.getOdontograma() != null ? detalle.getOdontograma().getId() : null)
                .numeroPieza(detalle.getNumeroPieza())
                .caraDental(detalle.getCaraDental())
                .condicion(detalle.getCondicion() != null ? detalle.getCondicion().name() : null)
                .descripcion(detalle.getDescripcion())
                .tratamientoPendiente(detalle.getTratamientoPendiente() != null
                        ? (detalle.getTratamientoPendiente() ? "S\u00ed" : "No") : null)
                .tratamientoRealizado(detalle.getTratamientoRealizado() != null
                        ? (detalle.getTratamientoRealizado() ? "S\u00ed" : "No") : null)
                .color(detalle.getColor())
                .fechaRegistro(detalle.getFechaRegistro())
                .build();
    }

    public List<OdontogramaDetalleResponse> toOdontogramaDetalleResponseList(List<OdontogramaDetalle> detalles) {
        if (detalles == null) return Collections.emptyList();
        return detalles.stream().map(this::toOdontogramaDetalleResponse).collect(Collectors.toList());
    }

    public PagoResponse toPagoResponse(Pago pago) {
        if (pago == null) return null;
        return PagoResponse.builder()
                .id(pago.getId())
                .numeroPago(pago.getNumeroPago())
                .pacienteId(pago.getPaciente() != null ? pago.getPaciente().getId() : null)
                .pacienteNombre(combinarNombreApellido(
                        pago.getPaciente() != null ? pago.getPaciente().getNombres() : null,
                        pago.getPaciente() != null ? pago.getPaciente().getApellidos() : null))
                .planTratamientoId(pago.getPlanTratamiento() != null ? pago.getPlanTratamiento().getId() : null)
                .tratamientoId(pago.getTratamiento() != null ? pago.getTratamiento().getId() : null)
                .monto(pago.getMonto())
                .fecha(pago.getFecha())
                .metodoPago(pago.getMetodoPago() != null ? pago.getMetodoPago().name() : null)
                .numeroOperacion(pago.getNumeroOperacion())
                .observaciones(pago.getObservaciones())
                .usuarioRegistroId(pago.getUsuarioRegistro() != null ? pago.getUsuarioRegistro().getId() : null)
                .usuarioRegistroNombre(combinarNombreApellido(
                        pago.getUsuarioRegistro() != null ? pago.getUsuarioRegistro().getNombres() : null,
                        pago.getUsuarioRegistro() != null ? pago.getUsuarioRegistro().getApellidos() : null))
                .estado(pago.getEstado() != null ? pago.getEstado().name() : null)
                .fechaCreacion(pago.getFechaCreacion())
                .build();
    }

    public List<PagoResponse> toPagoResponseList(List<Pago> pagos) {
        if (pagos == null) return Collections.emptyList();
        return pagos.stream().map(this::toPagoResponse).collect(Collectors.toList());
    }

    public RecetaResponse toRecetaResponse(Receta receta) {
        if (receta == null) return null;
        return RecetaResponse.builder()
                .id(receta.getId())
                .pacienteId(receta.getPaciente() != null ? receta.getPaciente().getId() : null)
                .pacienteNombre(combinarNombreApellido(
                        receta.getPaciente() != null ? receta.getPaciente().getNombres() : null,
                        receta.getPaciente() != null ? receta.getPaciente().getApellidos() : null))
                .odontologoId(receta.getOdontologo() != null ? receta.getOdontologo().getId() : null)
                .odontologoNombre(combinarNombreApellido(
                        receta.getOdontologo() != null ? receta.getOdontologo().getNombres() : null,
                        receta.getOdontologo() != null ? receta.getOdontologo().getApellidos() : null))
                .diagnostico(receta.getDiagnostico())
                .fecha(receta.getFecha())
                .observaciones(receta.getObservaciones())
                .aprobada(receta.getAprobada())
                .fechaAprobacion(receta.getFechaAprobacion() != null
                        ? receta.getFechaAprobacion().toLocalDate() : null)
                .activo(receta.getActivo())
                .detalles(java.util.Collections.emptyList())
                .fechaCreacion(receta.getFechaCreacion())
                .build();
    }

    public List<RecetaResponse> toRecetaResponseList(List<Receta> recetas) {
        if (recetas == null) return Collections.emptyList();
        return recetas.stream().map(this::toRecetaResponse).collect(Collectors.toList());
    }

    public RecetaDetalleResponse toRecetaDetalleResponse(RecetaDetalle detalle) {
        if (detalle == null) return null;
        return RecetaDetalleResponse.builder()
                .id(detalle.getId())
                .recetaId(detalle.getReceta() != null ? detalle.getReceta().getId() : null)
                .medicamentoId(detalle.getMedicamento() != null ? detalle.getMedicamento().getId() : null)
                .medicamentoNombre(detalle.getMedicamento() != null ? detalle.getMedicamento().getNombre() : null)
                .dosis(detalle.getDosis())
                .frecuencia(detalle.getFrecuencia())
                .duracion(detalle.getDuracion())
                .indicaciones(detalle.getIndicaciones())
                .orden(detalle.getOrden())
                .build();
    }

    public List<RecetaDetalleResponse> toRecetaDetalleResponseList(List<RecetaDetalle> detalles) {
        if (detalles == null) return Collections.emptyList();
        return detalles.stream().map(this::toRecetaDetalleResponse).collect(Collectors.toList());
    }

    public ProductoResponse toProductoResponse(Producto producto) {
        if (producto == null) return null;
        return ProductoResponse.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
                .descripcion(producto.getDescripcion())
                .unidadMedida(producto.getUnidadMedida())
                .stockActual(producto.getStockActual())
                .stockMinimo(producto.getStockMinimo())
                .precioCompra(producto.getPrecioCompra())
                .precioVenta(producto.getPrecioVenta())
                .fechaVencimiento(producto.getFechaVencimiento())
                .lote(producto.getLote())
                .proveedorId(producto.getProveedor() != null ? producto.getProveedor().getId() : null)
                .proveedorNombre(producto.getProveedor() != null ? producto.getProveedor().getRazonSocial() : null)
                .activo(producto.getActivo())
                .fechaCreacion(producto.getFechaCreacion())
                .build();
    }

    public List<ProductoResponse> toProductoResponseList(List<Producto> productos) {
        if (productos == null) return Collections.emptyList();
        return productos.stream().map(this::toProductoResponse).collect(Collectors.toList());
    }

    private String combinarNombreApellido(String nombres, String apellidos) {
        if (nombres == null && apellidos == null) return null;
        if (nombres == null) return apellidos;
        if (apellidos == null) return nombres;
        return nombres + " " + apellidos;
    }
}

