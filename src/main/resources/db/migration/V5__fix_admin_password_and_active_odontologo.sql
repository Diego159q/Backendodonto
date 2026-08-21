-- 1. Actualizar contraseña del administrador a admin123 (BCrypt hash valido)
UPDATE usuarios 
SET password = '$2a$10$UH4ICrTPb7MF4sc33olS9Ox3QOh9jq3z1cOZIfKDqROYYguRtIpvm' 
WHERE email = 'admin@dentalcare.com';

-- 2. Asegurar que todos los odontologos esten activos
UPDATE odontologos 
SET activo = TRUE 
WHERE activo IS NULL OR activo = FALSE;

-- 3. Asegurar que existan horarios de atencion de Lunes a Sabado
INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin, activo)
SELECT o.id, d.dia, '09:00'::time, '18:00'::time, TRUE
FROM odontologos o
CROSS JOIN (
    VALUES ('MONDAY'), ('TUESDAY'), ('WEDNESDAY'), ('THURSDAY'), ('FRIDAY')
) AS d(dia)
WHERE NOT EXISTS (
    SELECT 1 FROM horarios_atencion h 
    WHERE h.odontologo_id = o.id AND h.dia_semana = d.dia
);

INSERT INTO horarios_atencion (odontologo_id, dia_semana, hora_inicio, hora_fin, activo)
SELECT o.id, 'SATURDAY', '09:00'::time, '13:00'::time, TRUE
FROM odontologos o
WHERE NOT EXISTS (
    SELECT 1 FROM horarios_atencion h 
    WHERE h.odontologo_id = o.id AND h.dia_semana = 'SATURDAY'
);
