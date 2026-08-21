-- ============================================================
-- DENTALCARE SYSTEM - Datos Iniciales
-- PostgreSQL
-- ============================================================

-- Roles
INSERT INTO roles (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Acceso total al sistema'),
('ODONTOLOGA', 'Gestion clinica de pacientes'),
('RECEPCIONISTA', 'Gestion administrativa y de citas'),
('PACIENTE', 'Acceso limitado a informacion propia')
ON CONFLICT (nombre) DO NOTHING;

-- Usuario administrador por defecto
-- Password: admin123 (BCrypt encoded)
INSERT INTO usuarios (nombres, apellidos, email, username, password, rol_id, activo)
SELECT 'Administrador', 'Sistema', 'admin@dentalcare.com', 'admin',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       (SELECT id FROM roles WHERE nombre = 'ADMINISTRADOR'),
       TRUE
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@dentalcare.com');

-- Odontologo por defecto (vinculado al admin)
INSERT INTO odontologos (usuario_id, nombres, apellidos, dni, email, especialidad, numero_colegiatura)
SELECT (SELECT id FROM usuarios WHERE email = 'admin@dentalcare.com'),
       'Administrador', 'Sistema', '00000000', 'admin@dentalcare.com',
       'Odontologia General', 'COL-00001'
WHERE NOT EXISTS (SELECT 1 FROM odontologos WHERE numero_colegiatura = 'COL-00001');

-- Configuracion inicial del centro
INSERT INTO configuracion_centro (nombre_centro, duracion_cita_predeterminada, moneda)
SELECT 'Centro Odontologico DentalCare', 30, 'PEN'
WHERE NOT EXISTS (SELECT 1 FROM configuracion_centro);

-- Diagnosticos frecuentes
INSERT INTO diagnosticos (codigo, nombre, descripcion, activo) VALUES
('CAR-001', 'Caries Dental', 'Caries en una o mas piezas dentales', TRUE),
('GIN-001', 'Gingivitis', 'Inflamacion de las encias', TRUE),
('PER-001', 'Periodontitis', 'Infeccion periodontal avanzada', TRUE),
('PUL-001', 'Pulpitis', 'Inflamacion de la pulpa dental', TRUE),
('ABS-001', 'Absceso Dental', 'Infeccion con formacion de pus', TRUE),
('SEN-001', 'Sensibilidad Dental', 'Hipersensibilidad dentinaria', TRUE),
('BRU-001', 'Bruxismo', 'Rechinamiento de dientes', TRUE),
('MAL-001', 'Maloclusion', 'Mala alineacion dental', TRUE),
('HAL-001', 'Halitosis', 'Mal aliento cronico', TRUE),
('IMP-001', 'Impactacion Dental', 'Diente retenido sin erupcionar', TRUE)
ON CONFLICT (codigo) DO NOTHING;

-- Medicamentos frecuentes
INSERT INTO medicamentos (nombre, presentacion, concentracion, activo) VALUES
('Amoxicilina', 'Capsulas', '500mg', TRUE),
('Ibuprofeno', 'Tabletas', '400mg', TRUE),
('Paracetamol', 'Tabletas', '500mg', TRUE),
('Clorhexidina', 'Enjuague bucal', '0.12%', TRUE),
('Naproxeno', 'Tabletas', '500mg', TRUE),
('Metronidazol', 'Tabletas', '250mg', TRUE),
('Lidocaina', 'Solucion inyectable', '2%', TRUE),
('Diclofenaco', 'Tabletas', '50mg', TRUE),
('Ketorolaco', 'Tabletas', '10mg', TRUE),
('Penicilina', 'Tabletas', '500mg', TRUE)
ON CONFLICT DO NOTHING;

-- Categorias de productos
INSERT INTO categorias_productos (nombre, descripcion) VALUES
('Materiales de Restauracion', 'Resinas, composites, adhesivos'),
('Instrumental', 'Instrumentos de uso odontologico'),
('Anestesicos', 'Anestesicos locales y agujas'),
('Higiene Oral', 'Productos para higiene dental'),
('Equipos', 'Equipos odontologicos'),
('Protesico', 'Materiales para protesis'),
('Radiologia', 'Peliculas, reveladores'),
('Emergencia', 'Materiales de emergencia')
ON CONFLICT (nombre) DO NOTHING;

-- Tratamientos del catalogo
INSERT INTO tratamientos (nombre, descripcion, precio_base, numero_sesiones, activo) VALUES
('Limpieza Dental', 'Profilaxis y destartraje dental', 80.00, 1, TRUE),
('Curacion de Caries', 'Restauracion de caries con resina', 120.00, 1, TRUE),
('Extraccion Simple', 'Extraccion de pieza dental', 100.00, 1, TRUE),
('Endodoncia', 'Tratamiento de conducto', 350.00, 3, TRUE),
('Corona Dental', 'Colocacion de corona dental', 500.00, 2, TRUE),
('Protesis Parcial', 'Protesis dental removible', 800.00, 3, TRUE),
('Implante Dental', 'Colocacion de implante', 1200.00, 4, TRUE),
('Blanqueamiento Dental', 'Blanqueamiento con gel', 250.00, 2, TRUE),
('Sellante Dental', 'Aplicacion de sellante', 50.00, 1, TRUE),
('Ortodoncia', 'Tratamiento de ortodoncia', 1500.00, 12, TRUE),
('Ferula de Descarga', 'Ferula para bruxismo', 200.00, 2, TRUE),
('Examen Radiografico', 'Radiografia panoramica', 80.00, 1, TRUE)
ON CONFLICT DO NOTHING;
