ALTER TABLE tratamientos ADD COLUMN duracion_minutos INT DEFAULT 60;

CREATE TABLE horarios_atencion (
    id BIGSERIAL PRIMARY KEY,
    odontologo_id BIGINT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    activo BOOLEAN DEFAULT true,
    CONSTRAINT fk_horario_odontologo FOREIGN KEY (odontologo_id) REFERENCES odontologos(id)
);

CREATE TABLE bloqueos_agenda (
    id BIGSERIAL PRIMARY KEY,
    odontologo_id BIGINT,
    fecha DATE NOT NULL,
    hora_inicio TIME,
    hora_fin TIME,
    motivo VARCHAR(255),
    CONSTRAINT fk_bloqueo_odontologo FOREIGN KEY (odontologo_id) REFERENCES odontologos(id)
);

-- Insertar horarios base por defecto para el primer odontologo (L-V 09-18, S 09-13)
INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin)
SELECT id, 'MONDAY', '09:00', '18:00' FROM odontologos LIMIT 1;

INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin)
SELECT id, 'TUESDAY', '09:00', '18:00' FROM odontologos LIMIT 1;

INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin)
SELECT id, 'WEDNESDAY', '09:00', '18:00' FROM odontologos LIMIT 1;

INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin)
SELECT id, 'THURSDAY', '09:00', '18:00' FROM odontologos LIMIT 1;

INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin)
SELECT id, 'FRIDAY', '09:00', '18:00' FROM odontologos LIMIT 1;

INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin)
SELECT id, 'SATURDAY', '09:00', '13:00' FROM odontologos LIMIT 1;

-- Actualizar tratamientos existentes con tiempos de prueba realistas si son Limpieza, Evaluación, etc.
UPDATE tratamientos SET duracion_minutos = 30 WHERE nombre ILIKE '%Limpieza%' OR nombre ILIKE '%Evaluación%';
UPDATE tratamientos SET duracion_minutos = 90 WHERE nombre ILIKE '%Blanqueamiento%';
