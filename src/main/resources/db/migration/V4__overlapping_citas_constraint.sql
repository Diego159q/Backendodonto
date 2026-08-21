-- Habilitamos la extensión btree_gist si no existe, necesaria para poder incluir el odontologo_id 
-- y la fecha (que utilizan = ) junto con el rango de tiempo (que utiliza && para solapamiento) en el mismo índice de exclusión.
CREATE EXTENSION IF NOT EXISTS btree_gist;

-- Eliminamos el constraint anterior que solo protegía contra la misma hora exacta de inicio
DROP INDEX IF EXISTS idx_citas_odontologo_fecha_hora;

-- Agregamos la restricción de exclusión real para intervalos de tiempo usando tsrange (nativo)
ALTER TABLE citas
ADD CONSTRAINT exclude_overlapping_citas
EXCLUDE USING gist (
    odontologo_id WITH =,
    tsrange(fecha + hora_inicio, fecha + hora_fin) WITH &&
)
WHERE (estado != 'CANCELADA');
