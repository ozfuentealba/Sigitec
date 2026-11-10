# SIGITEC - Actividad Práctica 4 (proyecto integrador)

Sistema de Gestión de Incidencias Técnicas y Mantenimiento para Casinos. Prototipo académico **Java 17 + JDBC + MySQL 8**, desarrollado como continuidad de AP1, AP2 y AP3.

## Funciones

- Listar técnicos, salas, equipos e incidencias.
- Registrar una incidencia y asignarle prioridad; en el prototipo se elige automáticamente un técnico de ejemplo como responsable.
- Actualizar el estado; guardar el cambio en `historial_estado` cuando se usa MySQL.
- Buscar incidencias, ordenarlas por prioridad y atender pendientes en cola FIFO.
- **TP4:** mostrar resumen por prioridad (arreglo + `ArrayList`), exportar a CSV y consultar historial desde MySQL.
- Aplicar el patrón **Repository** mediante la interfaz `Repositorio<T>`; una fábrica sencilla elige la implementación en memoria o JDBC.

## Requisitos

- JDK 17 o superior (probado con OpenJDK 21 para verificación automática).
- Eclipse, NetBeans o IntelliJ; proyecto Maven (`pom.xml`).
- Para persistencia real: MySQL Server 8 y dependencia Maven `mysql-connector-j` (incluida en `pom.xml`).

## Abrir en Eclipse

`File > Import > Maven > Existing Maven Projects`, seleccionar la carpeta del proyecto.

- **Modo demostración:** ejecutar `ar.edu.sigitec.app.Main` sin argumentos.
- **Modo MySQL:** `Run > Run Configurations > Java Application > Main > Arguments`: escribir `--mysql`. En `Environment` agregar `SIGITEC_DB_PASSWORD` con la clave local. No subir esa clave a GitHub.

Configuración opcional de ambiente:

| Variable | Valor por defecto |
|---|---|
| `SIGITEC_DB_URL` | `jdbc:mysql://localhost:3306/sigitec_db?serverTimezone=America/Argentina/Buenos_Aires` |
| `SIGITEC_DB_USER` | `root` (solo para la práctica local; mejor crear usuario limitado) |
| `SIGITEC_DB_PASSWORD` | Sin valor; configurar de forma privada |

## Preparar la base de datos

Si es una **instalación nueva**, importar en este orden:

1. `database/01_esquema_AP2.sql`: define tablas, claves y relaciones.
2. `database/02_datos_demo_AP3.sql`: catálogos y ejemplos.

Si **ya existe** `sigitec_db` del TP3, **NO volver a ejecutar `01_esquema_AP2.sql`**. Conservá tus registros y ejecutá la nueva versión del programa. El archivo `database/04_validaciones_AP4.sql` contiene solo consultas de revisión y puede ejecutarse en esa base.

## Menú

| Opción | Función |
|---|---|
| 1-4 | Listar catálogos e incidencias |
| 5 | Registrar incidencia |
| 6 | Cambiar estado (incluye EN_PROCESO) |
| 7-8 | Búsqueda y ordenamiento |
| 9 | Atención pendiente FIFO |
| 10 | Resumen por prioridad (arreglo + ArrayList) |
| 11 | Exportación a `reportes/incidencias_tp4.csv` |
| 12 | Historial de estados (requiere `--mysql`) |
| 0 | Salir |

La exportación CSV usa `;` como separador y UTF-8. El archivo se guarda en `reportes` y esa carpeta está ignorada por Git para evitar publicar datos generados localmente.

## Ejecutar las pruebas

Desde Eclipse: `src/test/java/ar/edu/sigitec/PruebasSinDependencias.java` y `PruebasIntegradorasTP4.java` > **Run As > Java Application**.

Con JDK por terminal Unix (sin Maven):

```sh
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java src/test/java -name '*.java')
java -cp out ar.edu.sigitec.PruebasSinDependencias
java -cp out ar.edu.sigitec.PruebasIntegradorasTP4
java -cp out ar.edu.sigitec.app.Main
```

El resultado registrado en el entorno de verificación fue **8/8 TP3 y 15/15 TP4** (sin servidor MySQL). La prueba real JDBC con MySQL 8.0.46 y persistencia se realizó para el TP3 en la PC del estudiante. Las funciones nuevas del TP4 deben volver a comprobarse allí antes del video final.

## Qué mostrar en el video de 3 minutos

`docs/guion_video_tp4.md` contiene un guion natural y las acciones sugeridas. Mostrar el menú, la creación de un ticket, el cambio de estado, el resumen, el CSV y, en modo MySQL, el historial y la persistencia. **No grabar ni publicar contraseñas**.

## Alcance y límites

Prototipo académico, no implementación productiva. El programa no incorpora autenticación real de usuarios, edición completa de catálogos, roles de acceso ni control de concurrencia. El modo MySQL requiere datos de catálogo coherentes con el esquema del TP2. El ID de las nuevas incidencias se asigna desde la aplicación; para múltiples operadores se recomienda migrar a generación automática en MySQL. La capa JDBC usa `PreparedStatement` y transacciones para registrar cambios de estado e historial.

Repositorio de referencia: https://github.com/ozfuentealba/Sigitec. La versión TP4 se debe subir manualmente: generar/guardar un commit con el código de esta carpeta.
