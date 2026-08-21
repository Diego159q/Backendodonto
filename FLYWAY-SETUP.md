# Configuración de Flyway en Producción (Render)

Como la base de datos de producción ya existe y fue creada usando Hibernate (`ddl-auto=update`), ejecutar Flyway directamente intentaría crear de nuevo las tablas y fallaría porque ya existen.

Para evitar esto, necesitas marcar la base de datos de producción con la "línea base" (baseline). Esto le indica a Flyway que asuma que la migración `V1` ya fue aplicada.

Dado que tienes `spring.flyway.baseline-on-migrate=true` configurado en `application.properties`, **Flyway hará esto automáticamente por ti** la primera vez que la aplicación arranque en producción, siempre y cuando la tabla `flyway_schema_history` no exista aún.

### ¿Qué sucederá exactamente en Render?
1. La aplicación arrancará.
2. Flyway detectará que hay tablas existentes pero no está la tabla de control `flyway_schema_history`.
3. Al tener `baseline-on-migrate=true`, Flyway creará la tabla de control y marcará automáticamente la versión actual como `1` (que corresponde a tu `V1__baseline_from_init_sql.sql`).
4. Flyway **no** ejecutará `V1__baseline_from_init_sql.sql`.
5. Si en el futuro agregas un `V2__nueva_tabla.sql`, Flyway sí lo ejecutará normalmente.

### Ejecución Manual (Opcional)
Si por algún motivo necesitas forzar el baseline de forma manual mediante Maven, el comando sería:

```bash
mvn flyway:baseline \
  -Dflyway.url="jdbc:postgresql://<HOST_RENDER>:<PORT>/<DATABASE>" \
  -Dflyway.user="<USUARIO>" \
  -Dflyway.password="<PASSWORD>"
```

*Nota: Asegúrate de reemplazar las credenciales con las de tu base de datos en Render.*
