-- ============================================================
-- DENTALCARE SYSTEM - Esquema de Base de Datos
-- PostgreSQL
-- ============================================================

-- Crear base de datos (ejecutar si es necesario)
-- CREATE DATABASE dentalcare;
-- \c dentalcare

-- Extensiones
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- TABLAS
-- ============================================================

-- 1. Roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    foto_url VARCHAR(500),
    rol_id BIGINT NOT NULL REFERENCES roles(id),
    ultimo_acceso TIMESTAMP,
    bloqueado BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(rol_id);

-- 3. Odontologos
CREATE TABLE IF NOT EXISTS odontologos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuarios(id),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(150),
    especialidad VARCHAR(150),
    numero_colegiatura VARCHAR(50) NOT NULL UNIQUE,
    horario_atencion TEXT,
    firma_url VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    codigo_paciente VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE,
    fecha_nacimiento DATE,
    sexo VARCHAR(20),
    telefono VARCHAR(20),
    email VARCHAR(150),
    direccion TEXT,
    distrito VARCHAR(100),
    ciudad VARCHAR(100),
    estado_civil VARCHAR(30),
    ocupacion VARCHAR(100),
    tipo_sangre VARCHAR(10),
    alergias TEXT,
    enfermedades_previas TEXT,
    medicamentos_actuales TEXT,
    contacto_emergencia VARCHAR(100),
    telefono_emergencia VARCHAR(20),
    observaciones TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pacientes_dni ON pacientes(dni);
CREATE INDEX idx_pacientes_codigo ON pacientes(codigo_paciente);
CREATE INDEX idx_pacientes_nombres ON pacientes(nombres, apellidos);

-- 5. Configuracion Centro
CREATE TABLE IF NOT EXISTS configuracion_centro (
    id BIGSERIAL PRIMARY KEY,
    nombre_centro VARCHAR(200) NOT NULL DEFAULT 'Centro Odontologico DentalCare',
    ruc VARCHAR(20),
    direccion TEXT,
    telefono VARCHAR(20),
    email VARCHAR(150),
    logo_url VARCHAR(500),
    horario_atencion TEXT,
    duracion_cita_predeterminada INTEGER NOT NULL DEFAULT 30,
    moneda VARCHAR(10) NOT NULL DEFAULT 'PEN',
    mensaje_recordatorio TEXT,
    nombre_odontologa VARCHAR(200),
    colegiatura VARCHAR(50),
    firma_url VARCHAR(500),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. Citas
CREATE TABLE IF NOT EXISTS citas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    motivo VARCHAR(300) NOT NULL,
    tipo_atencion VARCHAR(100),
    consultorio VARCHAR(50),
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,
    motivo_cancelacion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_cita_estado CHECK (estado IN ('PENDIENTE','CONFIRMADA','ATENDIDA','CANCELADA','REPROGRAMADA','NO_ASISTIO')),
    CONSTRAINT ck_cita_horario CHECK (hora_inicio < hora_fin)
);

CREATE INDEX idx_citas_fecha ON citas(fecha);
CREATE INDEX idx_citas_paciente ON citas(paciente_id);
CREATE INDEX idx_citas_odontologo ON citas(odontologo_id);
CREATE INDEX idx_citas_estado ON citas(estado);

-- 7. Historias Clinicas
CREATE TABLE IF NOT EXISTS historias_clinicas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    fecha_apertura DATE NOT NULL DEFAULT CURRENT_DATE,
    motivo_consulta TEXT NOT NULL,
    enfermedad_actual TEXT,
    antecedentes_personales TEXT,
    antecedentes_familiares TEXT,
    alergias TEXT,
    enfermedades_sistemicas TEXT,
    presion_arterial VARCHAR(20),
    peso DECIMAL(5,2),
    talla DECIMAL(5,2),
    temperatura DECIMAL(4,1),
    diagnostico_general TEXT,
    observaciones TEXT,
    recomendaciones TEXT,
    odontologo_responsable_id BIGINT NOT NULL REFERENCES odontologos(id),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_hc_paciente ON historias_clinicas(paciente_id);

-- 8. Evoluciones Clinicas
CREATE TABLE IF NOT EXISTS evoluciones_clinicas (
    id BIGSERIAL PRIMARY KEY,
    historia_clinica_id BIGINT NOT NULL REFERENCES historias_clinicas(id),
    cita_id BIGINT REFERENCES citas(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    descripcion TEXT NOT NULL,
    procedimiento_realizado TEXT,
    observaciones TEXT,
    recomendaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. Odontogramas
CREATE TABLE IF NOT EXISTS odontogramas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    tipo_denticion VARCHAR(30) NOT NULL DEFAULT 'ADULTO',
    observaciones TEXT,
    estado VARCHAR(30) DEFAULT 'ACTIVO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 10. Odontograma Detalles
CREATE TABLE IF NOT EXISTS odontograma_detalles (
    id BIGSERIAL PRIMARY KEY,
    odontograma_id BIGINT NOT NULL REFERENCES odontogramas(id),
    numero_pieza INTEGER NOT NULL,
    cara_dental VARCHAR(20),
    condicion VARCHAR(50) NOT NULL DEFAULT 'SANO',
    descripcion TEXT,
    tratamiento_pendiente BOOLEAN NOT NULL DEFAULT FALSE,
    tratamiento_realizado BOOLEAN NOT NULL DEFAULT FALSE,
    color VARCHAR(20),
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_pieza_numero CHECK (numero_pieza BETWEEN 1 AND 52),
    CONSTRAINT ck_condicion CHECK (condicion IN (
        'SANO','CARIES','AUSENTE','EXTRACCION_INDICADA','EXTRACCION_REALIZADA',
        'CORONA','PROTESIS','IMPLANTE','RESINA','ENDODONCIA','FRACTURA',
        'SELLANTE','OBSERVACION'
    ))
);

-- 11. Diagnosticos
CREATE TABLE IF NOT EXISTS diagnosticos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 12. Paciente Diagnosticos
CREATE TABLE IF NOT EXISTS paciente_diagnosticos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    diagnostico_id BIGINT NOT NULL REFERENCES diagnosticos(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    pieza_dental INTEGER,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(30) DEFAULT 'ACTIVO',
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 13. Tratamientos (Catalogo)
CREATE TABLE IF NOT EXISTS tratamientos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio_base DECIMAL(10,2) DEFAULT 0,
    numero_sesiones INTEGER NOT NULL DEFAULT 1,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 14. Paciente Tratamientos
CREATE TABLE IF NOT EXISTS paciente_tratamientos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    diagnostico_id BIGINT REFERENCES paciente_diagnosticos(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    tratamiento_id BIGINT NOT NULL REFERENCES tratamientos(id),
    pieza_dental INTEGER,
    fecha_inicio DATE,
    fecha_fin_estimada DATE,
    fecha_fin_real DATE,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento DECIMAL(10,2) NOT NULL DEFAULT 0,
    precio_final DECIMAL(10,2) NOT NULL DEFAULT 0,
    numero_sesiones INTEGER NOT NULL DEFAULT 1,
    sesiones_realizadas INTEGER NOT NULL DEFAULT 0,
    porcentaje_avance INTEGER NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_ptr_estado CHECK (estado IN ('PENDIENTE','EN_PROCESO','TERMINADO','SUSPENDIDO','CANCELADO'))
);

CREATE INDEX idx_ptr_paciente ON paciente_tratamientos(paciente_id);
CREATE INDEX idx_ptr_odontologo ON paciente_tratamientos(odontologo_id);

-- 15. Planes de Tratamiento
CREATE TABLE IF NOT EXISTS planes_tratamiento (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    monto_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    monto_final DECIMAL(10,2) NOT NULL DEFAULT 0,
    adelanto DECIMAL(10,2) NOT NULL DEFAULT 0,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    aceptado_por_paciente BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_aceptacion TIMESTAMP,
    observaciones TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 16. Plan Tratamiento Detalles
CREATE TABLE IF NOT EXISTS plan_tratamiento_detalles (
    id BIGSERIAL PRIMARY KEY,
    plan_tratamiento_id BIGINT NOT NULL REFERENCES planes_tratamiento(id),
    paciente_tratamiento_id BIGINT NOT NULL REFERENCES paciente_tratamientos(id),
    pieza_dental INTEGER,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento DECIMAL(10,2) NOT NULL DEFAULT 0,
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    numero_sesiones INTEGER NOT NULL DEFAULT 1
);

-- 17. Pagos
CREATE TABLE IF NOT EXISTS pagos (
    id BIGSERIAL PRIMARY KEY,
    numero_pago VARCHAR(30) NOT NULL UNIQUE,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    plan_tratamiento_id BIGINT REFERENCES planes_tratamiento(id),
    paciente_tratamiento_id BIGINT REFERENCES paciente_tratamientos(id),
    monto DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    metodo_pago VARCHAR(30) NOT NULL,
    numero_operacion VARCHAR(100),
    observaciones TEXT,
    usuario_registro_id BIGINT REFERENCES usuarios(id),
    estado VARCHAR(20) NOT NULL DEFAULT 'PAGADO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_pago_metodo CHECK (metodo_pago IN ('EFECTIVO','YAPE','PLIN','TRANSFERENCIA','TARJETA')),
    CONSTRAINT ck_pago_estado CHECK (estado IN ('PENDIENTE','PAGADO','PARCIAL','ANULADO'))
);

CREATE INDEX idx_pagos_paciente ON pagos(paciente_id);
CREATE INDEX idx_pagos_fecha ON pagos(fecha);

-- 18. Cuotas
CREATE TABLE IF NOT EXISTS cuotas (
    id BIGSERIAL PRIMARY KEY,
    plan_tratamiento_id BIGINT NOT NULL REFERENCES planes_tratamiento(id),
    numero_cuota INTEGER NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    fecha_pago DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    pago_relacionado_id BIGINT REFERENCES pagos(id),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_cuota_estado CHECK (estado IN ('PENDIENTE','PAGADA','VENCIDA','ANULADA'))
);

-- 19. Medicamentos
CREATE TABLE IF NOT EXISTS medicamentos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    presentacion VARCHAR(100),
    concentracion VARCHAR(100),
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 20. Recetas
CREATE TABLE IF NOT EXISTS recetas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    odontologo_id BIGINT NOT NULL REFERENCES odontologos(id),
    diagnostico TEXT,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    observaciones TEXT,
    aprobada BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_aprobacion TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 21. Receta Detalles
CREATE TABLE IF NOT EXISTS receta_detalles (
    id BIGSERIAL PRIMARY KEY,
    receta_id BIGINT NOT NULL REFERENCES recetas(id),
    medicamento_id BIGINT NOT NULL REFERENCES medicamentos(id),
    dosis VARCHAR(100) NOT NULL,
    frecuencia VARCHAR(100) NOT NULL,
    duracion VARCHAR(100) NOT NULL,
    indicaciones TEXT,
    orden INTEGER NOT NULL DEFAULT 0
);

-- 22. Categorias Productos
CREATE TABLE IF NOT EXISTS categorias_productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 23. Productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    categoria_id BIGINT REFERENCES categorias_productos(id),
    descripcion TEXT,
    unidad_medida VARCHAR(30) NOT NULL DEFAULT 'UNIDAD',
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER NOT NULL DEFAULT 5,
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2),
    fecha_vencimiento DATE,
    lote VARCHAR(100),
    proveedor_id BIGINT REFERENCES proveedores(id) DEFERRABLE INITIALLY DEFERRED,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_productos_codigo ON productos(codigo);
CREATE INDEX idx_productos_stock ON productos(stock_actual);

-- 24. Movimientos Inventario
CREATE TABLE IF NOT EXISTS movimientos_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INTEGER NOT NULL,
    stock_anterior INTEGER NOT NULL,
    stock_nuevo INTEGER NOT NULL,
    motivo TEXT,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_registro_id BIGINT REFERENCES usuarios(id),
    CONSTRAINT ck_tipo_mov CHECK (tipo_movimiento IN ('ENTRADA','SALIDA','AJUSTE','DEVOLUCION','VENCIMIENTO'))
);

-- 25. Proveedores
CREATE TABLE IF NOT EXISTS proveedores (
    id BIGSERIAL PRIMARY KEY,
    razon_social VARCHAR(200) NOT NULL,
    ruc VARCHAR(20) UNIQUE,
    contacto VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(150),
    direccion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 26. Compras
CREATE TABLE IF NOT EXISTS compras (
    id BIGSERIAL PRIMARY KEY,
    proveedor_id BIGINT NOT NULL REFERENCES proveedores(id),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    numero_documento VARCHAR(50),
    monto_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    usuario_registro_id BIGINT REFERENCES usuarios(id),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 27. Compra Detalles
CREATE TABLE IF NOT EXISTS compra_detalles (
    id BIGSERIAL PRIMARY KEY,
    compra_id BIGINT NOT NULL REFERENCES compras(id),
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    lote VARCHAR(100),
    fecha_vencimiento DATE
);

-- 28. Recordatorios
CREATE TABLE IF NOT EXISTS recordatorios (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT REFERENCES pacientes(id),
    cita_id BIGINT REFERENCES citas(id),
    tipo VARCHAR(30) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_programada TIMESTAMP NOT NULL,
    enviado BOOLEAN NOT NULL DEFAULT FALSE,
    medio VARCHAR(30),
    fecha_envio TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_rec_tipo CHECK (tipo IN ('CITA','TRATAMIENTO','CONTROL','PAGO','CUOTA','LIMPIEZA_DENTAL')),
    CONSTRAINT ck_rec_medio CHECK (medio IN ('CORREO','NOTIFICACION_INTERNA','WHATSAPP'))
);

-- 29. Notificaciones
CREATE TABLE IF NOT EXISTS notificaciones (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    titulo VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    leida BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_lectura TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 30. Archivos Clinicos
CREATE TABLE IF NOT EXISTS archivos_clinicos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    historia_clinica_id BIGINT REFERENCES historias_clinicas(id),
    tipo_archivo VARCHAR(30) NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    url VARCHAR(500),
    tamano BIGINT,
    descripcion TEXT,
    fecha_subida TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_registro_id BIGINT REFERENCES usuarios(id),
    CONSTRAINT ck_tipo_archivo CHECK (tipo_archivo IN ('RADIOGRAFIA','FOTOGRAFIA','PDF','EXAMEN','OTRO'))
);

-- 31. Auditoria
CREATE TABLE IF NOT EXISTS auditoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuarios(id),
    accion VARCHAR(100) NOT NULL,
    entidad VARCHAR(100) NOT NULL,
    entidad_id BIGINT,
    descripcion TEXT,
    direccion_ip VARCHAR(50),
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    datos_anteriores TEXT,
    datos_nuevos TEXT
);

CREATE INDEX idx_auditoria_fecha ON auditoria(fecha);
CREATE INDEX idx_auditoria_entidad ON auditoria(entidad, entidad_id);

-- ============================================================
-- FUNCIONES
-- ============================================================

-- Funcion para actualizar fecha_actualizacion
CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers
CREATE TRIGGER trg_usuarios_update BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_pacientes_update BEFORE UPDATE ON pacientes
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_citas_update BEFORE UPDATE ON citas
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_historias_clinicas_update BEFORE UPDATE ON historias_clinicas
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_odontogramas_update BEFORE UPDATE ON odontogramas
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_paciente_tratamientos_update BEFORE UPDATE ON paciente_tratamientos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_pagos_update BEFORE UPDATE ON pagos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_productos_update BEFORE UPDATE ON productos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_proveedores_update BEFORE UPDATE ON proveedores
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_recetas_update BEFORE UPDATE ON recetas
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_planes_tratamiento_update BEFORE UPDATE ON planes_tratamiento
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
CREATE TRIGGER trg_configuracion_update BEFORE UPDATE ON configuracion_centro
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();
