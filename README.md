# DentalCare Backend - Fase 2

Backend del Sistema de Gestión para Centro Odontológico.

## Arquitectura

```
Controller  -->  Service (Interface + Impl)  -->  Repository  -->  Database
                        |
                   MapperUtil
                        |
                    DTO (Request/Response)
```

## Capas

| Capa | Paquete | Responsabilidad |
|------|---------|----------------|
| Controller | `controller/` | Endpoints REST, validación de entrada |
| Service | `service/` + `service/impl/` | Lógica de negocio |
| Repository | `repository/` | Acceso a datos JPA |
| Entity | `entity/` | Modelo de datos JPA |
| DTO | `dto/request/` + `dto/response/` | Objetos de transferencia |
| Mapper | `mapper/` | Conversión Entity ↔ DTO |
| Security | `security/` | JWT, autenticación, autorización |
| Config | `config/` | CORS, Swagger, Jackson |
| Exception | `exception/` | Manejo global de errores |
| Enums | `enums/` | Constantes del dominio |

## Módulos implementados (27)

1. Autenticación (AuthService)
2. Usuarios (UsuarioService)
3. Odontólogos (OdontologoService)
4. Pacientes (PacienteService)
5. Citas (CitaService)
6. Historias Clínicas (HistoriaClinicaService)
7. Evoluciones Clínicas (EvolucionClinicaService)
8. Odontogramas (OdontogramaService)
9. Diagnósticos (DiagnosticoService)
10. Tratamientos - Catálogo (TratamientoService)
11. Tratamientos - Paciente (PacienteTratamientoService)
12. Planes de Tratamiento (PlanTratamientoService)
13. Pagos (PagoService)
14. Cuotas (CuotaService)
15. Recetas (RecetaService)
16. Medicamentos (MedicamentoService)
17. Productos (ProductoService)
18. Movimientos de Inventario (MovimientoInventarioService)
19. Proveedores (ProveedorService)
20. Compras (CompraService)
21. Recordatorios (RecordatorioService)
22. Notificaciones (NotificacionService)
23. Archivos Clínicos (ArchivoClinicoService)
24. Configuración del Centro (ConfiguracionService)
25. Auditoría (AuditoriaService)
26. Dashboard (DashboardService)
27. Reportes (ReporteService)

## Endpoints principales (25 controladores)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/auth/login` | Iniciar sesión |
| POST | `/auth/register` | Registrar usuario |
| GET | `/usuarios` | Listar usuarios |
| GET | `/pacientes` | Listar pacientes |
| POST | `/pacientes` | Crear paciente |
| GET | `/pacientes/{id}` | Obtener paciente |
| GET | `/citas` | Listar citas (con filtros) |
| POST | `/citas` | Crear cita |
| PATCH | `/citas/{id}/confirmar` | Confirmar cita |
| PATCH | `/citas/{id}/cancelar` | Cancelar cita |
| GET | `/citas/horarios-disponibles` | Horarios libres |
| GET | `/citas/hoy` | Citas del día |
| POST | `/historias-clinicas` | Crear historia clínica |
| POST | `/odontogramas` | Crear odontograma |
| POST | `/odontogramas/{id}/detalles` | Agregar detalle |
| GET | `/diagnosticos` | Listar diagnósticos |
| GET | `/tratamientos/catalogo` | Catálogo de tratamientos |
| POST | `/tratamientos` | Asignar tratamiento |
| POST | `/planes-tratamiento` | Crear plan |
| POST | `/pagos` | Registrar pago |
| GET | `/pagos/ingresos/dia` | Ingresos del día |
| POST | `/recetas` | Crear receta |
| PATCH | `/recetas/{id}/aprobar` | Aprobar receta |
| GET | `/productos` | Listar productos |
| GET | `/productos/stock-bajo` | Stock bajo |
| POST | `/compras` | Registrar compra |
| GET | `/reportes/dashboard` | Dashboard |
| GET | `/reportes/{tipo}` | Reportes |
| GET | `/configuracion` | Configuración (público) |

## Base de datos

31 tablas PostgreSQL con esquema completo en `resources/db/schema.sql`.

## Seguridad

- JWT con expiración configurable
- BCrypt para contraseñas
- Control de acceso por roles (ADMINISTRADOR, ODONTOLOGA, RECEPCIONISTA, PACIENTE)
- Endpoints públicos: `/auth/login`, `/auth/recuperar-password`, `/auth/restablecer-password`, `/configuracion`

## Ejecución local

```bash
# Requisitos
# - Java 17+
# - Maven 3.8+
# - PostgreSQL 14+

# 1. Crear base de datos
psql -U postgres -c "CREATE DATABASE dentalcare;"
psql -U postgres -d dentalcare -f src/main/resources/db/schema.sql
psql -U postgres -d dentalcare -f src/main/resources/db/data.sql

# 2. Configurar variables de entorno
set DB_URL=jdbc:postgresql://localhost:5432/dentalcare
set DB_USERNAME=postgres
set DB_PASSWORD=tu_contraseña
set JWT_SECRET=UnaClaveSeguraDe256BitsParaJWT

# 3. Ejecutar
mvn clean install
mvn spring-boot:run
```

## Supabase

```properties
# Configuración para Supabase
DB_URL=jdbc:postgresql://<host>.supabase.co:5432/postgres?sslmode=require
DB_USERNAME=postgres
DB_PASSWORD=<contraseña_supabase>
```

Pasos:
1. Crear proyecto en [Supabase](https://supabase.com)
2. Ir a Settings → Database → Connection string
3. Copiar la cadena URI
4. Ejecutar `schema.sql` y `data.sql` en el Editor SQL de Supabase
5. Configurar variables de entorno con los datos de Supabase

## Swagger

```
http://localhost:8080/swagger-ui/index.html
```

## Pruebas

```bash
mvn test
```

Las pruebas unitarias cubren:
- Registro y búsqueda de pacientes
- Validación de cruce de horarios en citas
- Creación y cancelación de citas
- Registro de pagos y validación de montos
- Flujo de autenticación (login)
- Control de acceso por roles
