-- Migración para prevenir doble reserva exacta en base de datos.
-- Crea un índice único parcial para asegurar que un odontólogo no pueda tener 
-- dos citas a la misma hora exacta (a menos que una esté cancelada).
-- Nota: La validación de superposición de rangos de tiempo se maneja en capa de aplicación con Locking.

CREATE UNIQUE INDEX idx_citas_odontologo_fecha_hora 
ON citas(odontologo_id, fecha, hora_inicio) 
WHERE estado != 'CANCELADA';
